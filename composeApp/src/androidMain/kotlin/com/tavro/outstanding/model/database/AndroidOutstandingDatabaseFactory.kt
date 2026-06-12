package com.tavro.outstanding.model.database

import android.content.Context
import androidx.room.Room

class AndroidOutstandingDatabaseFactory(
    private val context: Context,
    // TODO: private val dispatchers: Dispatchers
) : OutstandingDatabase.Factory {
    override fun create(): OutstandingDatabase {
        return Room.databaseBuilder(context, OutstandingDatabase::class.java, OutstandingDatabase.NAME)
            // TODO(XXX): .setQueryCoroutineContext(dispatchers.diskParallel)
            .build()
    }
}
