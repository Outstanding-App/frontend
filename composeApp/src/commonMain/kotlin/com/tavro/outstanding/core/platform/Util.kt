package com.tavro.outstanding.core.platform

expect val platform: Platform
val isIOS = platform == Platform.IOS
val isAndroid = platform == Platform.ANDROID

expect val buildType: BuildType
val isDebuggable: Boolean
    get() = buildType.isDebug

expect val platformName: String

const val IOS_BUNDLE_ID = "com.tavro.outstanding"
