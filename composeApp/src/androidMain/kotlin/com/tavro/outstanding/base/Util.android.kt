package com.tavro.outstanding.base

actual val platform: Platform = Platform.ANDROID

actual val buildType: BuildType = BuildType.parse("debug") // TODO(XXX): Use generated build config

actual val platformName = "Google"
