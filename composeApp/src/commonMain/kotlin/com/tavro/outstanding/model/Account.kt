package com.tavro.outstanding.model

import com.tavro.outstanding.base.time.Clock
import com.tavro.outstanding.model.database.account.AccountEntity

data class Account(
    val id: Long = 0L,
    val authToken: String? = null,
    val createdAt: Long = Clock.currentTimeMillis()
) {
    companion object {
        val EMPTY = Account(
            id = -1
        )

        internal fun fromEntity(entity: AccountEntity) = Account(
            id = entity.id,
            authToken = entity.authToken,
            createdAt = entity.createdAt,
        )
    }
}

internal fun Account.toEntity() = AccountEntity(
    id = id,
    authToken = authToken,
    createdAt = createdAt
)
