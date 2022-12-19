package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.databinding.ActivityAddBengkelBinding
import com.example.tubespbp.databinding.ActivityEditBengkelBinding
import com.example.tubespbp.databinding.ActivityEditProfileBinding
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import server.api.BengkelApi
import server.models.Bengkel
import java.nio.charset.StandardCharsets
import java.util.HashMap

class EditBengkelActivity : AppCompatActivity() {

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEditBengkelBinding = ActivityEditBengkelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var id = intent.getIntExtra("idBengkel", -1) // ambil id bengkel

        val inputNama = binding.inputLayoutNama
        val inputAlamat = binding.inputLayoutAlamat
        val inputJam = binding.inputLayoutJamOperasional
        val inputJenis = binding.inputLayoutJenis

        val btnCancel = binding.cancelBtn
        val btnEdit = binding.editBengkelBtn

        btnCancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", intent.getIntExtra("id", -1))
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userData", bundle)
            startActivity(intent)
        }

        btnEdit.setOnClickListener(View.OnClickListener {
            var checkEdit = false

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
                checkEdit = true
            }

            if (!checkEdit){return@OnClickListener}

            val bengkel = Bengkel(id, nama, alamat, jam, jenis)

            val stringRequest: StringRequest = object : StringRequest(Method.PUT, BengkelApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val bengkel = gson.fromJson(response, Bengkel::class.java)

                    if(bengkel != null){
                        FancyToast.makeText(this@EditBengkelActivity, "Berhasil edit bengkel", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                        val bundle = Bundle()
                        bundle.putInt("id", intent.getIntExtra("id", -1))
                        val moveHome = Intent(this, HomeActivity::class.java)
                        moveHome.putExtra("userData", bundle)
                        startActivity(moveHome)
                    }

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditBengkelActivity,
                            errors.getString("Error: message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Log.d("Error Regist", e.message.toString())
                        Toast.makeText(this@EditBengkelActivity,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    //params["jumlahTiket"] = inputField?.text.toString()
                    params["nama"] = nama
                    params["alamat"] = alamat
                    params["jamOperasional"] = jam
                    params["jenis"] = jenis
                    return params
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queue!!.add(stringRequest)
        })
    }
}