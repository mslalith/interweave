package dev.mslalith.gradle

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroid() {
    android {
        compileSdkVersion(libs.getVersion("android-compileSdk").toInt())

        defaultConfig {
            minSdk = libs.getVersion("android-minSdk").toIntOrNull()
            targetSdk = libs.getVersion("android-targetSdk").toIntOrNull()
        }

//        compileOptions {
//            // https://developer.android.com/studio/write/java8-support
//            isCoreLibraryDesugaringEnabled = true
//        }
    }

//    dependencies {
//        // https://developer.android.com/studio/write/java8-support
//        "coreLibraryDesugaring"(libs.findLibrary("desugarJdkLibs").get())
//    }
}

private fun Project.android(action: BaseExtension.() -> Unit) = extensions.configure<BaseExtension>(action)