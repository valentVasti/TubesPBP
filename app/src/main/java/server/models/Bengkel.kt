package server.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Bengkel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var nama: String,
    var alamat: String,
    var jamOperasional: String,
    var jenis: String
    )
