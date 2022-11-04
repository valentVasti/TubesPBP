package com.example.tubespbp

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.example.tubespbp.databinding.ActivityRegistActiviyBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_regist_activiy.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.util.*

class RegistActiviy : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    val db by lazy { UserDB(this) }
    private var noteId: Int = 0
    private val regChannel = "regChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_regist_activiy
        val binding: ActivityRegistActiviyBinding = ActivityRegistActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val inputUsername = binding.inputLayoutUsername
        val inputPassword = binding.inputLayoutPassword
        val inputEmail = binding.inputLayoutEmail
        val inputBirthDate = binding.inputLayoutTTL
        val inputPhone = binding.inputLayoutNoTelp

        val btnBack = binding.backBtn
        val btnRegis = binding.btnRegist
        val datePicker = binding.inputBirthDate

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(datePicker)
            }
        }

        datePicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@RegistActiviy,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegis.setOnClickListener(View.OnClickListener {
            var checkRegist = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val birthDate: String = inputBirthDate.getEditText()?.getText().toString()
            val phone: String = inputPhone.getEditText()?.getText().toString()

            val loginData = Bundle()

            if (username.isEmpty()) {
                inputUsername.setError("Username tidak boleh kosong")
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password tidak boleh kosong")
            }

            if (email.isEmpty()) {
                inputEmail.setError("Email tidak boleh kosong")
            }

            if (birthDate.isEmpty()) {
                inputBirthDate.setError("Tanggal lahir tidak boleh kosong")
            }

            if (phone.isEmpty()) {
                inputPhone.setError("Nomor Telepon tidak boleh kosong")
            }

            if (
                !username.isEmpty() &&
                !password.isEmpty() &&
                !email.isEmpty() &&
                !birthDate.isEmpty() &&
                !phone.isEmpty()
            ) {
                checkRegist = true
            }

            if (!checkRegist) return@OnClickListener
            //setupListener()

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().addUser(
                    User(
                        0,
                        username,
                        password,
                        email,
                        birthDate,
                        phone
                    )
                )
                finish()
            }
//            loginData.putString("username", username)
//            loginData.putString("password", password)

            val moveLogin = Intent(this, LoginActivity::class.java)
            sendNotification()
            startActivity(moveLogin)
        })
    }

    private fun setupListener() {
        btnRegist.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().addUser(
                    User(
                        0,
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString(),
                        inputEmail.getText().toString(),
                        inputBirthDate.getText().toString(),
                        inputPhone.getText().toString()
                    )
                )
                finish()
            }
        }
    }

    private fun updateDateInView(editDate: TextInputEditText) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editDate.setText(sdf.format(cal.getTime()).toString())
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val registChannel = NotificationChannel(regChannel, name, NotificationManager.IMPORTANCE_DEFAULT).apply{
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

        val builder = NotificationCompat.Builder(this, regChannel)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setContentTitle("Register Success!")
            .setContentText("Silahkan lanjutkan ke Login")
            .setStyle(bigPictureStyle)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
//            .addAction(R.mipmap.ic_launcher, "Login", pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }
    }

