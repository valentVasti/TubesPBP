package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.datepicker.MaterialDatePicker
//import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class RegistActiviy : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputBirthDate: TextInputLayout
    private lateinit var inputPhone: TextInputLayout
    private lateinit var editDate: TextInputEditText
    private lateinit var mainLayout: ConstraintLayout

    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_activiy)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        inputEmail = findViewById(R.id.inputLayoutEmail)
        inputBirthDate = findViewById(R.id.inputLayoutTTL)
        inputPhone = findViewById(R.id.inputLayoutNoTelp)

        val btnBack = findViewById<Button>(R.id.backBtn)
        val btnRegis = findViewById<Button>(R.id.btnRegist)
        val datePicker = findViewById<TextInputEditText>(R.id.inputBirthDate)

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
                checkRegist = true;
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
