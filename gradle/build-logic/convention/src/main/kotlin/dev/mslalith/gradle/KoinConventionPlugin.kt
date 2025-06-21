package dev.mslalith.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("ksp"))
            apply(KotlinMultiplatformConventionPlugin::class)
            apply(KoinCoreConventionPlugin::class)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.getLibrary("koin-annotations"))
            }

            sourceSets.commonMain.configure {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }
        }

        extensions.configure<KspExtension> {
            arg("KOIN_CONFIG_CHECK", "false")
        }

        addKspDependencyForAllTargets(libs.getLibrary("koin-compiler"))
    }
}