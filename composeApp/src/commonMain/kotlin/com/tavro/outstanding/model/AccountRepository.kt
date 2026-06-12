package com.tavro.outstanding.model

import com.tavro.outstanding.model.database.account.AccountDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepository(private val dao: AccountDao) {
    suspend fun save(account: Account): Long = dao.save(account.toEntity())

    suspend fun update(account: Account) = dao.update(account.toEntity())

    suspend fun updateAuthToken(id: Long, authToken: String?) = dao.updateAuthToken(id, authToken)

    fun getAccountFlow(): Flow<Account?> = dao.getAccountFlow().map { entity ->
        entity?.let(Account::fromEntity)
    }

    suspend fun getAccounts(): List<Account> = dao.getAccounts().map(Account::fromEntity)

    suspend fun clear() = dao.clear()
}
