package com.tavro.outstanding.core.koin

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

/** Routes [getKoin] to the isolated [OutstandingKoinContext] instead of Koin's global instance. */
interface OutstandingKoinComponent : KoinComponent {
    override fun getKoin(): Koin = OutstandingKoinContext.koin
}
