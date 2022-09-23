package com.example.tubespbp.queue

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import java.util.concurrent.Flow

class QueueRepository(private val queuesDao: QueueDao) {

    val allQueue: LiveData<List<Queue>> = queuesDao.getAllQueues()

    suspend fun insert(queue: Queue) {
        queuesDao.insert(queue)
    }


    suspend fun delete(queue: Queue){
        queuesDao.delete(queue)
    }


    suspend fun update(queue: Queue){
        queuesDao.update(queue)
    }
}