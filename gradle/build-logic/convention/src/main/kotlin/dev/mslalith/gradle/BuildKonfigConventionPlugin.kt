package dev.mslalith.gradle

import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

private val KEYS = listOf("SUPABASE_URL", "SUPABASE_KEY")

class BuildKonfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("buildkonfig"))
        }

        val secretProperties = target.readSecretProperties()
        secretProperties.ensureRequiredKeys()

        extensions.configure<BuildKonfigExtension> {
            packageName = "dev.mslalith.interweave"

            defaultConfigs {
                KEYS.forEach { key ->
                    buildConfigField(FieldSpec.Type.STRING, key, secretProperties[key].toString())
                }
            }
        }
    }

    private fun Project.readSecretProperties(): Properties {
        val secretPropertiesFile = rootProject.file("secret.properties")
        if (!secretPropertiesFile.exists()) throw IllegalStateException("secret.properties file is required")

        val secretProperties = Properties()
        FileInputStream(secretPropertiesFile).use { fis -> secretProperties.load(fis) }
        return secretProperties
    }

    private fun Properties.ensureRequiredKeys() {
        for (key in KEYS) if (this[key] == null) throw IllegalStateException("$key field is required in secret.properties")
    }
}
