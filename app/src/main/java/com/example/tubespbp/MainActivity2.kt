
package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tubespbp.queue.Queue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.queue.QueueViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity2 : AppCompatActivity(), QueueRVAdapter.QueueClickInterface,
    QueueRVAdapter.QueueClickDeleteInterface {

    lateinit var viewModal: QueueViewModel
    lateinit var queuesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        queuesRV = findViewById(R.id.queueRV)
        addFAB = findViewById(R.id.idFAB)


        queuesRV.layoutManager = LinearLayoutManager(this)


        val queueRVAdapter = QueueRVAdapter(this, this, this)

        queuesRV.adapter = queueRVAdapter


        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QueueViewModel::class.java)

        viewModal.allQueues.observe(this, Observer { list ->
            list?.let {

                queueRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity2, AddEditQueueActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onQueueClick(queue: Queue) {
        val intent = Intent(this@MainActivity2, AddEditQueueActivity::class.java)
        intent.putExtra("queueType", "Edit")
        intent.putExtra("queueNama", queue.nama)
        intent.putExtra("queueJenis", queue.jenisKendaraan)
        intent.putExtra("queueId", queue.ID)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(queue: Queue) {

        viewModal.deleteQueue(queue)

        Toast.makeText(this, "${queue.nama} Deleted", Toast.LENGTH_LONG).show()
    }
}