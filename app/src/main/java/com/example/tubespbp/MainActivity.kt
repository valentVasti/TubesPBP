package com.example.tubespbp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var layoutUsername : TextInputLayout
    private lateinit var layoutPassword : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val loginData = intent.extras
        setText(loginData)

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

        btnLogin.setOnClickListener(View.OnClickListener{
            var checkLogin = false
            val loginData = intent.extras

            var inputUsername = layoutUsername.getEditText()?.getText().toString()
            var inputPassword = layoutPassword.getEditText()?.getText().toString()

            if(inputUsername.isEmpty())
                layoutUsername.setError("Username must be filled with text")

            if(inputPassword.isEmpty())
                layoutPassword.setError("Password must be filled with text")

            if (loginData != null) {
                if(
                    inputUsername == loginData.getString("username")
                    && inputPassword == loginData.getString("password")
                ){checkLogin = true}
                else if(
                    inputUsername != loginData.getString("username")
                    && inputPassword == loginData.getString("password")
                ){layoutUsername.setError("Username salah!")}
                else if(
                    inputUsername == loginData.getString("username")
                    && inputPassword != loginData.getString("password")
                ){layoutPassword.setError("Password salah!")}
            }else if(!inputUsername.isEmpty() && !inputPassword.isEmpty()){
                val moveRegist = Intent(this, RegistActiviy::class.java)
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Login Gagal")
                    .setMessage("Note: Belum ada akun terdaftar")
                    .setPositiveButton("Buat Akun", object : DialogInterface.OnClickListener{
                        override fun onClick(dialogInterface: DialogInterface, i: Int){
                            startActivity(moveRegist)
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            if(!checkLogin)
                return@OnClickListener
            else{
                val moveHome = Intent(this, HomeActivity::class.java)
                startActivity(moveHome)
            }
        })
    }

    fun setText(loginData: Bundle?){
        val editUsername: TextInputEditText = findViewById(R.id.inputUsername)

        if(loginData!=null) {
            editUsername.setText(loginData.getString("username"))
        }
    }
}
