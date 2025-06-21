package dev.mslalith.gradle

import org.gradle.api.artifacts.Dependency
import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any): Dependency? = add(
    configurationName = "implementation",
    dependencyNotation = dependencyNotation
)