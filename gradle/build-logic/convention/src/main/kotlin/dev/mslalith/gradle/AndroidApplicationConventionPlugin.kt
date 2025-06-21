package dev.mslalith.gradle

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("androidApplication"))
        }

        configureAndroid()
        configureAndroidApplication()
    }
}

private fun Project.configureAndroidApplication() {
    android {
        namespace = "dev.mslalith.interweave"
        compileSdk = libs.getVersion("android-compileSdk").toInt()

        defaultConfig {
            applicationId = "dev.mslalith.interweave"
            minSdk = libs.getVersion("android-minSdk").toIntOrNull()
            targetSdk = libs.getVersion("android-targetSdk").toIntOrNull()
            versionCode = 1
            versionName = "1.0"
        }

        buildFeatures {
            compose = true
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}

private fun Project.android(action: BaseAppModuleExtension.() -> Unit) = extensions.configure<BaseAppModuleExtension>(action)