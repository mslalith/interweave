package dev.mslalith.gradle.extensions

import org.gradle.api.plugins.PluginManager

val PluginManager.isAndroidApplication: Boolean
    get() = hasPlugin("com.android.application")

val PluginManager.isAndroidLibrary: Boolean
    get() = hasPlugin("com.android.library")

val PluginManager.hasAndroidPlugin: Boolean
    get() = isAndroidApplication || isAndroidLibrary