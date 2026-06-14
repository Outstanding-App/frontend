package com.tavro.outstanding.data.account

import com.tavro.outstanding.data.account.local.AccountDao
import com.tavro.outstanding.data.account.local.AccountEntity
import com.tavro.outstanding.domain.account.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepository(private val dao: AccountDao) {
    suspend fun save(account: Account): Long = dao.save(account.toEntity())

    suspend fun update(account: Account) = dao.update(account.toEntity())

    suspend fun updateAuthToken(id: Long, authToken: String?) = dao.updateAuthToken(id, authToken)

    fun getAccountFlow(): Flow<Account?> = dao.getAccountFlow().map { it?.toDomain() }

    suspend fun getAccounts(): List<Account> = dao.getAccounts().map { it.toDomain() }

    suspend fun clear() = dao.clear()

    private fun AccountEntity.toDomain() = Account(
        id = id,
        authToken = authToken,
        createdAt = createdAt,
    )

    private fun Account.toEntity() = AccountEntity(
        id = id,
        authToken = authToken,
        createdAt = createdAt,
    )
}
