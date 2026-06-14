package com.tavro.outstanding.domain.account

import com.tavro.outstanding.core.time.Clock

data class Account(
    val id: Long = 0L,
    val authToken: String? = null,
    val createdAt: Long = Clock.currentTimeMillis()
) {
    companion object {
        val EMPTY = Account(id = -1)
    }
}
