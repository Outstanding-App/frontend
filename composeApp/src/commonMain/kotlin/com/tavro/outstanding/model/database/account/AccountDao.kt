package com.tavro.outstanding.model.database.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert
    suspend fun save(account: AccountEntity): Long

    @Update
    suspend fun update(account: AccountEntity)

    @Query("UPDATE accounts SET auth_token = :authToken WHERE id = :id")
    suspend fun updateAuthToken(id: Long, authToken: String?)

    @Query("SELECT * FROM accounts ORDER BY id DESC LIMIT 1")
    fun getAccountFlow(): Flow<AccountEntity?>

    @Query("SELECT * FROM accounts")
    suspend fun getAccounts(): List<AccountEntity>

    @Query("DELETE FROM accounts")
    suspend fun clear()
}
