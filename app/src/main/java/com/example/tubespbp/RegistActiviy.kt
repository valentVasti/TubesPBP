package com.example.tubespbp

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
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
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import server.api.UserApi
import java.util.*
//import server.models.User
import java.nio.charset.StandardCharsets

class RegistActiviy : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private var queue: RequestQueue? = null
    private val regChannel = "regChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            queue = Volley.newRequestQueue(this)

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val birthDate: String = inputBirthDate.getEditText()?.getText().toString()
            val phone: String = inputPhone.getEditText()?.getText().toString()

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

            if (!checkRegist){return@OnClickListener}

            val user = User(0, username, password, email, birthDate, phone)

            val stringRequest: StringRequest = object : StringRequest(Method.POST,UserApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val user = gson.fromJson(response, User::class.java)

                    if(user != null){
                        FancyToast.makeText(this@RegistActiviy, "Register Berhasil", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
//                        sendNotification()
                    }

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@RegistActiviy,
                            errors.getString("Error: message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Log.d("Error Regist", e.message.toString())
                        Toast.makeText(this@RegistActiviy,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queue!!.add(stringRequest)

            val moveLogin = Intent(this, LoginActivity::class.java)
//            sendNotification()
            startActivity(moveLogin)
        })
    }

    private fun updateDateInView(editDate: TextInputEditText) {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editDate.setText(sdf.format(cal.getTime()).toString())
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val registChannel = NotificationChannel(regChannel, name, importance).apply{
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

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this@RegistActiviy, 0, intent, 0)
        val bigPictureBitmap = ContextCompat.getDrawable(this@RegistActiviy, R.drawable.big_picture)?.toBitmap()
        val bigPictureStyle = NotificationCompat.BigPictureStyle().bigPicture(bigPictureBitmap)

        val builder = NotificationCompat.Builder(this, regChannel)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setContentTitle("Register Success!")
            .setContentText("Silahkan lanjutkan ke Login")
            .setStyle(bigPictureStyle)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }
    }

