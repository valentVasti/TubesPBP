package com.example.tubespbp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val birth: String,
    val phone: String
)