package com.example.tubespbp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }, 2000)


        if(!sharedPreferences!!.contains("check")){
            editSharedP("true")

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }, 2000)
        }

        if(sharedPreferences!!.getString("check","") == "true"){
            editSharedP("false")
        }else{
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
        }

    }

    private fun editSharedP(check: String){

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()

        editor.putString("check", check)
        editor.apply()
    }
}