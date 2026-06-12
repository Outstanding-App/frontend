package com.tavro.outstanding.di

import com.tavro.outstanding.model.AccountProvider
import com.tavro.outstanding.model.AccountRepository
import com.tavro.outstanding.model.database.OutstandingDatabase
import org.koin.dsl.bind
import org.koin.dsl.module

val modelModule = module {
    single { get<OutstandingDatabase.Factory>().create() }
    single { get<OutstandingDatabase>().accountDao() }
    single { AccountRepository(get()) }
    single { AccountProvider.Impl(get(), get()) } bind AccountProvider::class
}
