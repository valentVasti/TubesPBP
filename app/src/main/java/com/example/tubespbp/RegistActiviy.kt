package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.example.tubespbp.databinding.ActivityRegistActiviyBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.example.tubespbp.room.UserDao
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_regist_activiy.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistActiviy : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    val db by lazy { UserDB(this) }
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_regist_activiy
        val binding: ActivityRegistActiviyBinding = ActivityRegistActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputUsername = binding.inputLayoutUsername
        val inputPassword = binding.inputLayoutPassword
        val inputEmail = binding.inputLayoutEmail
        val inputBirthDate = binding.inputLayoutTTL
        val inputPhone = binding.inputLayoutNoTelp

        val btnBack = binding.backBtn
        val btnRegis = binding.btnRegist
        val datePicker = binding.inputBirthDate

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(datePicker)
            }
        }

        datePicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@RegistActiviy,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegis.setOnClickListener(View.OnClickListener{
            var checkRegist = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val birthDate: String = inputBirthDate.getEditText()?.getText().toString()
            val phone: String = inputPhone.getEditText()?.getText().toString()

            val loginData = Bundle()

            if(username.isEmpty()){
                inputUsername.setError("Username tidak boleh kosong")
            }

            if(password.isEmpty()){
                inputPassword.setError("Password tidak boleh kosong")
            }

            if(email.isEmpty()){
                inputEmail.setError("Email tidak boleh kosong")
            }

            if(birthDate.isEmpty()){
                inputBirthDate.setError("Tanggal lahir tidak boleh kosong")
            }

            if(phone.isEmpty()){
                inputPhone.setError("Nomor Telepon tidak boleh kosong")
            }

            if(
                !username.isEmpty() &&
                !password.isEmpty() &&
                !email.isEmpty() &&
                !birthDate.isEmpty() &&
                !phone.isEmpty()
                ){
                checkRegist = true
                btnRegis.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.userDao().addUser(
                            User(0,username,password,email,birthDate,phone)
                        )
                        finish()
                    }
                }
            }

            if(!checkRegist) return@OnClickListener

            loginData.putString("username", username)
            loginData.putString("password", password)

            val moveLogin = Intent(this, MainActivity::class.java)
            MaterialAlertDialogBuilder(this@RegistActiviy)
                .setTitle("Akun berhasil dibuat!")
                .setMessage("Note: Silahkan lanjutkan ke login")
                .setPositiveButton("Login", object : DialogInterface.OnClickListener{
                    override fun onClick(dialogInterface: DialogInterface, i: Int){
                        moveLogin.putExtras(loginData)
                        startActivity(moveLogin)
                    }
                })
                .show()
         })
    }


    private fun updateDateInView(editDate: TextInputEditText) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editDate.setText(sdf.format(cal.getTime()).toString())
    }
}
