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
import com.example.tubespbp.databinding.ActivityAddBengkelBinding
import com.example.tubespbp.databinding.ActivityRegistActiviyBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.android.gms.common.util.Clock
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import server.api.BengkelApi
import server.api.UserApi
import server.models.Bengkel
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class AddBengkelActivity : AppCompatActivity() {

    private var queue: RequestQueue? = null
    private val notifChannel = "addBengkelChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddBengkelBinding = ActivityAddBengkelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val inputNama = binding.inputLayoutNama
        val inputAlamat = binding.inputLayoutAlamat
        val inputJam = binding.inputLayoutJamOperasional
        val inputJenis = binding.inputLayoutJenis

        val btnCancel = binding.cancelBtn
        val btnAdd = binding.addBengkelBtn

        btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener(View.OnClickListener {
            var checkAdd = false

            queue = Volley.newRequestQueue(this)

            val nama: String = inputNama.getEditText()?.getText().toString()
            val alamat: String = inputAlamat.getEditText()?.getText().toString()
            val jam: String = inputJam.getEditText()?.getText().toString()
            val jenis: String = inputJenis.getEditText()?.getText().toString()

            if (nama.isEmpty()) {
                inputNama.setError("Nama bengkel tidak boleh kosong")
            }

            if (alamat.isEmpty()) {
                inputAlamat.setError("Alamat tidak boleh kosong")
            }

            if (jam.isEmpty()) {
                inputJam.setError("Jam Operasional tidak boleh kosong")
            }

            if (jenis.isEmpty()) {
                inputJenis.setError("Jenis bengkel tidak boleh kosong")
            }

            if (
                !nama.isEmpty() &&
                !alamat.isEmpty() &&
                !jam.isEmpty() &&
                !jenis.isEmpty()
            ) {
                checkAdd = true
            }

            if (!checkAdd){return@OnClickListener}

            val bengkel = Bengkel(nama, alamat, jam, jenis)

            val stringRequest: StringRequest = object : StringRequest(Method.POST, BengkelApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val bengkel = gson.fromJson(response, Bengkel::class.java)

                    if(bengkel != null){
                        FancyToast.makeText(this@AddBengkelActivity, "Berhasil tambah bengkel", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                        sendNotification()
                    }

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@AddBengkelActivity,
                            errors.getString("Error: message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Log.d("Error Regist", e.message.toString())
                        Toast.makeText(this@AddBengkelActivity,e.message, Toast.LENGTH_LONG).show()
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
                    val requestBody = gson.toJson(bengkel)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queue!!.add(stringRequest)

            val moveHome = Intent(this, HomeActivity::class.java)
            sendNotification()
            startActivity(moveHome)
        })
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val addChannel = NotificationChannel(notifChannel, name, importance).apply{
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(addChannel)
        }
    }

    private fun sendNotification(){

        val intent: Intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, notifChannel)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setContentTitle("Berhasil menambah 1 Bengkel!")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())
        }
    }
}