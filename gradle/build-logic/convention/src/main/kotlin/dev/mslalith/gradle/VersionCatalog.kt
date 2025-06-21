package dev.mslalith.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.compose: ComposePlugin.Dependencies
    get() = ComposePlugin.Dependencies(project = this)

internal fun VersionCatalog.getPluginId(alias: String): String = findPlugin(alias).get().get().pluginId
internal fun VersionCatalog.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> = findLibrary(alias).get()

internal fun VersionCatalog.getVersion(alias: String): String = findVersion(alias).get().requiredVersion