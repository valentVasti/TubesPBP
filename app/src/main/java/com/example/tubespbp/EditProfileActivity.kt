package com.example.tubespbp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.databinding.ActivityEditProfileBinding
import com.example.tubespbp.room.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import loginRegis.LoginActivity
import org.json.JSONObject
import server.api.UserApi
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private var queue: RequestQueue? = null
    private val editChannel = "editChannel"
    
    private var tvUsername: TextInputLayout? = null
    private var tvPassword: TextInputLayout? = null
    private var tvEmail: TextInputLayout? = null
    private var tvBirthdate: TextInputLayout? = null
    private var tvPhone: TextInputLayout? = null

    private lateinit var binding: ActivityEditProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        var id = intent.getIntExtra("id", -1)

        setText(id)

        val inputUsername = binding.inputLayoutUsername
        val inputPassword = binding.inputLayoutPassword
        val inputEmail = binding.inputLayoutEmail
        val inputBirthDate = binding.inputLayoutTTL
        val inputPhone = binding.inputLayoutNoTelp

        val btnCancel = binding.cancelBtn
        val btnEdit = binding.btnEdit
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
                    this@EditProfileActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })

        btnCancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", id)
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userData", bundle)
            startActivity(intent)
        }

        btnEdit.setOnClickListener(View.OnClickListener {
            var checkEdit = false

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
                checkEdit = true
            }

            if (!checkEdit){return@OnClickListener}

            val user = User(id, username, password, email, birthDate, phone)

            val stringRequest: StringRequest = object : StringRequest(Method.PUT, UserApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val user = gson.fromJson(response, User::class.java)

                    if(user != null){
                        FancyToast.makeText(this@EditProfileActivity, "Update Berhasil", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    }

                    sendNotification()
                    val bundle = Bundle()
                    bundle.putInt("id", id)

                    val moveHome = Intent(this, HomeActivity::class.java)
                    moveHome.putExtra("userData", bundle)
                    startActivity(moveHome)

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditProfileActivity,
                            errors.getString("Error: message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Log.d("Error Regist", e.message.toString())
                        Toast.makeText(this@EditProfileActivity,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(user)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }

                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    //params["jumlahTiket"] = inputField?.text.toString()
                    params["username"] = username
                    params["password"] = password
                    params["email"] = email
                    params["birth"] = birthDate
                    params["phone"] = phone
                    return params
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queue!!.add(stringRequest)
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

            val registChannel = NotificationChannel(editChannel, name, importance).apply{
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

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this@EditProfileActivity, 0, intent, 0)
        val bigPictureBitmap = ContextCompat.getDrawable(this@EditProfileActivity, R.drawable.big_picture)?.toBitmap()
        val bigPictureStyle = NotificationCompat.BigPictureStyle().bigPicture(bigPictureBitmap)

        val builder = NotificationCompat.Builder(this, editChannel)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setContentTitle("Edit Profile Success!")
            .setStyle(bigPictureStyle)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }

    private fun setText(id: Int){

        queue = Volley.newRequestQueue(this)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                val user: User = userList[0]
                binding.inputUsername.setText(user.username)
                binding.inputPassword.setText(user.password)
                binding.inputEmail.setText(user.email)
                binding.inputBirthDate.setText(user.birth)
                binding.inputPhone.setText(user.phone)
//
//                tvUsername!!.setT
//                tvPassword!!.setText(user.password)
//                tvEmail!!.setText(user.email)
//                tvBirthdate!!.setText(user.birth)
//                tvPhone!!.setText(user.phone)

            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Log.d("Error Show Profile:", error.message!!)
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }
}