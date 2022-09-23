package com.example.tubespbp.queue

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface QueueDao {

    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(queue :Queue)
    
    @Delete
    suspend fun delete(queue: Queue)
    
    @Query("Select * from queueTables order by ID ASC")
    fun getAllQueues(): LiveData<List<Queue>>
    
    @Update
    suspend fun update(queue: Queue)

}