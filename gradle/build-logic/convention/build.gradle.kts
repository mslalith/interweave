plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "dev.mslalith.android.application"
            implementationClass = "dev.mslalith.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "dev.mslalith.android.library"
            implementationClass = "dev.mslalith.gradle.AndroidLibraryConventionPlugin"
        }

        register("androidTest") {
            id = "dev.mslalith.android.test"
            implementationClass = "dev.mslalith.gradle.AndroidTestConventionPlugin"
        }

        register("kotlinAndroid") {
            id = "dev.mslalith.kotlin.android"
            implementationClass = "dev.mslalith.gradle.KotlinAndroidConventionPlugin"
        }

        register("kotlinMultiplatform") {
            id = "dev.mslalith.kotlin.multiplatform"
            implementationClass = "dev.mslalith.gradle.KotlinMultiplatformConventionPlugin"
        }

        register("compose") {
            id = "dev.mslalith.compose"
            implementationClass = "dev.mslalith.gradle.ComposeMultiplatformConventionPlugin"
        }

        register("koinCore") {
            id = "dev.mslalith.koin.core"
            implementationClass = "dev.mslalith.gradle.KoinCoreConventionPlugin"
        }

        register("koin") {
            id = "dev.mslalith.koin"
            implementationClass = "dev.mslalith.gradle.KoinConventionPlugin"
        }
    }
}