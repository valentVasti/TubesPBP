package com.example.tubespbp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private val logChannel = "logChannel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        //setText()
        val dontHaveAccount = findViewById<TextView>(R.id.dontHaveAccount)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val editUsername: TextInputEditText = findViewById(R.id.inputUsername)
        val editPassword: TextInputEditText = findViewById(R.id.inputPassword)
        val layoutUsername: TextInputLayout = findViewById(R.id.inputLayoutUsername)
        val layoutPassword: TextInputLayout = findViewById(R.id.inputLayoutPassword)
/*
        editUsername.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                layoutUsername.setError(null)
            }
        })

 */
        dontHaveAccount.setOnClickListener {
            val moveRegist = Intent(this, RegistActiviy::class.java)
            startActivity(moveRegist)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false

            val inputUsername = layoutUsername.getEditText()?.getText().toString()
            val inputPassword = layoutPassword.getEditText()?.getText().toString()

            if (inputUsername.isEmpty())
                layoutUsername.setError("Username must be filled with text")

            if (inputPassword.isEmpty())
                layoutPassword.setError("Password must be filled with text")

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty()){
                val user: User = findLoginData(inputUsername)

                Log.d("user",user.toString())

                if(user.id != 99){
                    if(inputPassword == user.password){
                        checkLogin = true
                    }else{
                        layoutPassword.setError("Password Salah!")
                    }
                }else{
                    layoutUsername.setError("Username tidak ditemukan!")
                }

//            if (loginData != null) {
//                if (
//                    inputUsername == loginData.getString("username")
//                    && inputPassword == loginData.getString("password")
//                ) {
//                    checkLogin = true
//                } else if (
//                    inputUsername != loginData.getString("username")
//                    && inputPassword == loginData.getString("password")
//                ) {
//                    layoutUsername.setError("Username salah!")
//                } else if (
//                    inputUsername == loginData.getString("username")
//                    && inputPassword != loginData.getString("password")
//                ) {
//                    layoutPassword.setError("Password salah!")
//                }
//            } else if (!inputUsername.isEmpty() && !inputPassword.isEmpty()) {
//                val moveRegist = Intent(this, RegistActiviy::class.java)
//                MaterialAlertDialogBuilder(this@LoginActivity)
//                    .setTitle("Login Gagal")
//                    .setMessage("Note: Belum ada akun terdaftar")
//                    .setPositiveButton("Buat Akun", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
//                            startActivity(moveRegist)
//                        }
//                    })
//                    .setNegativeButton("Cancel", null)
//                    .show()
//            }
                if (!checkLogin)
                    return@OnClickListener
                else {
                    val moveHome = Intent(this, HomeActivity::class.java)
                    startActivity(moveHome)
                }
            }

        })
    }

    fun setText() {
        val editUsername: TextInputEditText = findViewById(R.id.inputUsername)
        var user = User(0,"a","a","","","")

        CoroutineScope(Dispatchers.IO).launch {
            user = db.userDao().getUser(0)[0]}

        editUsername.setText(user.username)
    }

    private fun findLoginData(data: String): User {
        var user = User(0, "a", "a", "", "", "")

        CoroutineScope(Dispatchers.IO).launch {
            var userList: List<User> = db.userDao().getUserByUsername(data)

            if(!userList.isEmpty()){
                user = userList.get(0)
            }
        }
        return user
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val registChannel = NotificationChannel(logChannel, name, NotificationManager.IMPORTANCE_DEFAULT).apply{
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(registChannel)
        }
    }

    private fun sendNotification(){

        val intent: Intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val bigPictureBitmap = ContextCompat.getDrawable(this, R.drawable.big_picture)?.toBitmap()
        val bigPictureStyle = NotificationCompat.BigPictureStyle().bigPicture(bigPictureBitmap)

        val builder = NotificationCompat.Builder(this, logChannel)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setContentTitle("Login Success!")
            .setContentText("")
            .setStyle(bigPictureStyle)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }
}
