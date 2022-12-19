package server.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Queue (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var no_antrian: Int,
    var id_bengkel: Int,
    var id_user: Int,
    var kerusakan: String,
    var tanggal: String
    )
