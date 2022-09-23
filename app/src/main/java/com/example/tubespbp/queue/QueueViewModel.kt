package com.example.tubespbp.queue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QueueViewModel (application: Application) :AndroidViewModel(application) {


    val allQueues : LiveData<List<Queue>>
    val repository : QueueRepository

    init {
        val dao = QueueDB.getDatabase(application).getQueueDao()
        repository = QueueRepository(dao)
        allQueues = repository.allQueue
    }

    fun deleteQueue (queue: Queue) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(queue)
    }

    fun updateQueue(queue: Queue) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(queue)
    }

    fun addQueue(queue: Queue) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(queue)
    }
}