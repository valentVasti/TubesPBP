package com.example.tubespbp.queue
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "queueTables")

class Queue (
    @PrimaryKey(autoGenerate = true)
    var ID: Int,
    val nama: String,
    val jenisKendaraan: String
)
