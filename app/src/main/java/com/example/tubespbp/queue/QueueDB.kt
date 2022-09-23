package com.example.tubespbp.queue

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Queue::class), version = 1, exportSchema = false)
abstract class QueueDB : RoomDatabase() {

    abstract fun getQueueDao(): QueueDao

    companion object {
        @Volatile
        private var INSTANCE: QueueDB? = null

        fun getDatabase(context: Context): QueueDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QueueDB::class.java,
                    "queue_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}