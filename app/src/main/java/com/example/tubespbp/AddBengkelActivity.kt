package com.example.tubespbp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tubespbp.databinding.ActivityRegistActiviyBinding
import com.example.tubespbp.room.UserDB
import com.google.android.gms.common.util.Clock

class AddBengkelActivity : AppCompatActivity() {


    val  db by lazy { UserDB(this) }
    private var noteId: Int = 0
    private val regChannel = "regChannel"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistActiviyBinding = ActivityRegistActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputivBengkel = binding.inputLayoutEmail
    }
}