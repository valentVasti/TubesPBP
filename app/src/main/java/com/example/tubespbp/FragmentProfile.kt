package com.example.tubespbp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tubespbp.room.UserDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentProfile : Fragment () {
    val db by lazy { UserDB(requireActivity()) }
    val loginData = intent.extras
    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch{
            val users = db.userDao().getUserByUsername(loginData.getString("username"))
            tv_username.setText(users.username)
            tv_email.setText(users.email)
            tv_birth.setText(users.birth)
            tv_phone.setText(users.phone)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadData()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}