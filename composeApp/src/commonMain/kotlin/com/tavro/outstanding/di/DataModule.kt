package com.tavro.outstanding.di

import com.tavro.outstanding.data.account.AccountRepository
import com.tavro.outstanding.data.database.OutstandingDatabase
import com.tavro.outstanding.domain.account.AccountProvider
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { get<OutstandingDatabase.Factory>().create() }
    single { get<OutstandingDatabase>().accountDao() }
    single { AccountRepository(get()) }
    single { AccountProvider.Impl(get(), get()) } bind AccountProvider::class
}
