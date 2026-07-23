package com.tavro.outstanding.data.account.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "auth_token", defaultValue = "NULL")
    val authToken: String? = null,
    @ColumnInfo(name = "created_at", defaultValue = "0")
    val createdAt: Long = 0L,
)
