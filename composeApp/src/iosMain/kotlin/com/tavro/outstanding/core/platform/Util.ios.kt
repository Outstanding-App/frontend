package com.tavro.outstanding.core.platform

actual val platform: Platform = Platform.IOS

actual val buildType: BuildType = BuildType.parse("debug") // TODO(XXX): Use generated build config

actual val platformName = "Apple"
