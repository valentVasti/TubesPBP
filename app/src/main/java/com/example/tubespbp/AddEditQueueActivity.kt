package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tubespbp.queue.QueueViewModel
import com.example.tubespbp.queue.Queue


class AddEditQueueActivity : AppCompatActivity() {

    lateinit var queueNamaEdt: EditText
    lateinit var queueJenisEdt: EditText
    lateinit var saveBtn: Button

    lateinit var viewModal: QueueViewModel
    var queueId = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_queue)


        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QueueViewModel::class.java)



        queueNamaEdt = findViewById(R.id.editNama)
        queueJenisEdt = findViewById(R.id.editJenis)
        saveBtn = findViewById(R.id.idBtn)


        val queueType = intent.getStringExtra("queueNama")
        if (queueType.equals("Edit")) {

            val queueNama = intent.getStringExtra("queueNama")
            val queueJenis = intent.getStringExtra("queueJenis")
            queueId = intent.getIntExtra("queueID", -1)
            saveBtn.setText("Update Queue")
            queueNamaEdt.setText(queueNama)
            queueJenisEdt.setText(queueJenis)
        } else {
            saveBtn.setText("Save Queue")
        }

        saveBtn.setOnClickListener {

            val queueNama = queueNamaEdt.text.toString()
            val queueJenis = queueJenisEdt.text.toString()

            if (queueType.equals("Edit")) {
                if (queueNama.isNotEmpty() && queueJenis.isNotEmpty()) {
                    val updatedQueue = Queue(0,queueNama, queueJenis)
                    updatedQueue.ID = queueId
                    viewModal.updateQueue(updatedQueue)
                    Toast.makeText(this, "Queue Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (queueNama.isNotEmpty() && queueJenis.isNotEmpty()) {
                    viewModal.addQueue(Queue(0,queueNama, queueJenis))
                    Toast.makeText(this, "$queueNama Added", Toast.LENGTH_LONG).show()
                }
            }

            startActivity(Intent(applicationContext, MainActivity2::class.java))
            this.finish()
        }
    }
}