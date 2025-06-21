package dev.mslalith.gradle

import com.android.build.gradle.internal.lint.AndroidLintAnalysisTask
import com.android.build.gradle.internal.lint.LintModelWriterTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply(libs.getPluginId("composeMultiplatform"))
            apply(libs.getPluginId("composeCompiler"))
            apply(libs.getPluginId("composeHotReload"))
        }

        configureCompose()
    }
}

private fun Project.configureCompose() {
    composeCompiler {
        // Needed for Layout Inspector to be able to see all of the nodes in the component tree:
        //https://issuetracker.google.com/issues/338842143
        includeSourceInformation.set(true)

        if (project.providers.gradleProperty("enableComposeCompilerReports").isPresent) {
            val composeReports = layout.buildDirectory.map { it.dir("reports").dir("compose") }
            reportsDestination.set(composeReports)
            metricsDestination.set(composeReports)
        }

        stabilityConfigurationFiles.add(project.layout.projectDirectory.file("compose-stability.conf"))
    }

    // Workaround for:
    // Task 'generateDebugUnitTestLintModel' uses this output of task
    // 'generateResourceAccessorsForAndroidUnitTest' without declaring an explicit or
    // implicit dependency.
    tasks.matching { it is AndroidLintAnalysisTask || it is LintModelWriterTask }.configureEach {
        mustRunAfter(tasks.matching { it.name.startsWith("generateResourceAccessorsFor") })
    }
}

fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}