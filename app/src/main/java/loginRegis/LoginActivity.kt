package loginRegis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import com.example.tubespbp.HomeActivity
import com.example.tubespbp.R
//import server.models.User
import com.example.tubespbp.room.User
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import server.api.UserApi
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {
    private var queue: RequestQueue? = null
    private val logChannel = "logChannel"
    private lateinit var user: User
    var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        queue= Volley.newRequestQueue(this)

        val dontHaveAccount = findViewById<TextView>(R.id.dontHaveAccount)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val layoutUsername: TextInputLayout = findViewById(R.id.inputLayoutUsername)
        val layoutPassword: TextInputLayout = findViewById(R.id.inputLayoutPassword)

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
                val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
                    Response.Listener { response->
                        val gson = Gson()
                        val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                        if(userList.isEmpty()){
                            FancyToast.makeText(this@LoginActivity, "Belum ada data User", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                        }else{
                            for(user in userList){
                                if(inputUsername == user.username){
                                    if(inputPassword == user.password){
                                        checkLogin = true
                                        val bundle = Bundle()

//                                        sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
//                                        sharedPreferences!!.edit().putString("username",user.username)

                                        bundle.putInt("id", user.id)

                                        FancyToast.makeText(this@LoginActivity, "Login Berhasil", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                        val moveHome = Intent(this, HomeActivity::class.java)
                                        moveHome.putExtra("userData", bundle)
//                                        sendNotification()
                                        startActivity(moveHome)
                                        break
                                    }else{
                                        FancyToast.makeText(this@LoginActivity, "Password Salah!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                                        break
                                    }
                                }
                            }

                            if(!checkLogin){
                                FancyToast.makeText(this@LoginActivity, "Username tidak ditemukan!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                                return@Listener
                            }
                        }
                    }, Response.ErrorListener { error->
                        try{
                            val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@LoginActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }catch (e: Exception){
                            Log.d("Error Login", e.message.toString())
                            Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_SHORT).show()
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
    })

    }

//    fun setText() {
//        val editUsername: TextInputEditText = findViewById(R.id.inputUsername)
//        var user = User(99,"","","","","")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            user = db.userDao().getUser(0)[0]}
//
//        editUsername.setText(user.username)
//    }
//
//    private fun findLoginData(data: String): User {
//        return user
//    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val registChannel = NotificationChannel(logChannel, name, NotificationManager.IMPORTANCE_HIGH).apply{
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
