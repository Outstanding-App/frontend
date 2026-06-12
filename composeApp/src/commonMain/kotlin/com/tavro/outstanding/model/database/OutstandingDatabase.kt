package com.tavro.outstanding.model.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.tavro.outstanding.model.database.account.AccountDao
import com.tavro.outstanding.model.database.account.AccountEntity

// Room KMP requires an expect/actual constructor object but the actual implementation is generated
// at compile time, so the expect declaration has no handwritten actual.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object OutstandingDatabaseConstructor : RoomDatabaseConstructor<OutstandingDatabase> {
    override fun initialize(): OutstandingDatabase
}

@Database(
    exportSchema = true,
    entities = [
        AccountEntity::class
    ],
    version = 1
)
@ConstructedBy(OutstandingDatabaseConstructor::class)
abstract class OutstandingDatabase : RoomDatabase() {
    companion object {
        const val NAME = "outstanding.sqlite3"
    }

    interface Factory {
        fun create(): OutstandingDatabase
    }

    abstract fun accountDao(): AccountDao
}
