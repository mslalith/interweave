dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

buildCache {
    val isCi = providers.environmentVariable("CI").isPresent

    local {
        isEnabled = !isCi
    }
}

include(":convention")