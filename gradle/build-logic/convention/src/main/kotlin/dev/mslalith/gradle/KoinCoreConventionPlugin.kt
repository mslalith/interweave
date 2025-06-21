package dev.mslalith.gradle

import dev.mslalith.gradle.extensions.hasAndroidPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinCoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(KotlinMultiplatformConventionPlugin::class)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.findLibrary("koin-core").get())
            }

            if (pluginManager.hasAndroidPlugin) {
                sourceSets.androidMain.dependencies {
                    implementation(libs.findLibrary("koin-android").get())
                }
            }
        }
    }
}