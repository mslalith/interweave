package dev.mslalith.gradle

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("ksp"))
            apply(libs.getPluginId("room"))
        }

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.getLibrary("room.runtime"))
                implementation(libs.getLibrary("room.ktx"))
                implementation(libs.getLibrary("sqlite.bundled"))
            }
        }

        addKspDependencyForAllTargets(libs.getLibrary("room.compiler"))
    }
}