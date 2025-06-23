package dev.mslalith.gradle

import dev.mslalith.gradle.extensions.hasAndroidPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class KotlinMultiplatformConventionPlugin : Plugin<Project> {

    @Suppress("LongMethod")
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("kotlinMultiplatform"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            if (pluginManager.hasAndroidPlugin) {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }
            }

            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            ).forEach { iosTarget ->
                iosTarget.binaries.framework {
                    baseName = "ComposeApp"
                    isStatic = true
                    linkerOpts.add("-lsqlite3")
                }
            }

            jvm("desktop")

            targets.withType<KotlinNativeTarget>().configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            // https://kotlinlang.org/docs/whatsnew19.html#compiler-option-for-c-interop-implicit-integer-conversions
                            freeCompilerArgs.add("-XXLanguage:+ImplicitSignedToUnsignedIntegerConversion")

                            // Enable debug symbols:
                            // https://kotlinlang.org/docs/native-ios-symbolication.html
                            freeCompilerArgs.add("-Xadd-light-debug=enable")

                            // Various opt-ins
                            freeCompilerArgs.addAll(
                                "-opt-in=kotlinx.cinterop.ExperimentalForeignApi",
                                "-opt-in=kotlinx.cinterop.BetaInteropApi",
                            )
                            freeCompilerArgs.add("-Xcontext-receivers")
                        }
                    }
                }
            }

            targets.configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                            freeCompilerArgs.add("-Xcontext-receivers")
                            freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
                        }
                    }
                }
            }

            configureKotlin()
        }
    }
}

fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) = addKspDependencyForAllTargets("", dependencyNotation)
fun Project.addKspTestDependencyForAllTargets(dependencyNotation: Any) = addKspDependencyForAllTargets("Test", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                add(
                    "ksp${target.targetName.capitalized()}$configurationNameSuffix",
                    dependencyNotation,
                )
            }
    }
}