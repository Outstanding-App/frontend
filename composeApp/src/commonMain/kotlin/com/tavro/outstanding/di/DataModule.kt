package com.tavro.outstanding.di

import com.tavro.outstanding.data.account.AccountRepository
import com.tavro.outstanding.data.account.AuthService
import com.tavro.outstanding.data.account.HttpAuthService
import com.tavro.outstanding.data.database.OutstandingDatabase
import com.tavro.outstanding.domain.account.AccountProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) { json() }
        }
    }
    single { HttpAuthService(get()) } bind AuthService::class
    single { get<OutstandingDatabase.Factory>().create() }
    single { get<OutstandingDatabase>().accountDao() }
    single { AccountRepository(get()) }
    single { AccountProvider.Impl(get(), get()) } bind AccountProvider::class
}
