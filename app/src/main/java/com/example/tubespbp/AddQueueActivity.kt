package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.databinding.ActivityAddBengkelBinding
import com.example.tubespbp.databinding.ActivityAddQueueBinding
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import server.api.BengkelApi
import server.api.QueueApi
import server.models.Bengkel
import server.models.Queue
import java.nio.charset.StandardCharsets
import java.util.HashMap

class AddQueueActivity : AppCompatActivity() {

    private var queueReq: RequestQueue? = null
    private val notifChannel = "addBengkelChannel"
    private var edBengkel: AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddQueueBinding = ActivityAddQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", -1)

        val inputNoAntrian = binding.inputLayoutNoAntrian
        val inputBengkel = binding.inputLayoutBengkel
        val inputKerusakan = binding.inputLayoutKerusakan
        val inputTanggal = binding.inputLayoutTanggal

        val btnCancel = binding.cancelBtn
        val btnAdd = binding.addQueueBtn

        btnCancel.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", intent.getIntExtra("id", -1))
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userData", bundle)
            startActivity(intent)
        }

        btnAdd.setOnClickListener{
            var checkAdd = false

            queueReq = Volley.newRequestQueue(this)

            val noAntrian: String = inputNoAntrian.getEditText()?.getText().toString()
            val bengkel: String = inputBengkel.getEditText()?.getText().toString()
            val kerusakan: String = inputKerusakan.getEditText()?.getText().toString()
            val tanggal: String = inputTanggal.getEditText()?.getText().toString()

            if (noAntrian.isEmpty()) {
                inputNoAntrian.setError("No Antrian tidak boleh kosong")
            }

            if (bengkel.isEmpty()) {
                inputBengkel.setError("Bengkel tidak boleh kosong")
            }

            if (kerusakan.isEmpty()) {
                inputKerusakan.setError("Kerusakan tidak boleh kosong")
            }

            if (tanggal.isEmpty()) {
                inputTanggal.setError("Tanggal tidak boleh kosong")
            }

            if (
                !noAntrian.isEmpty() &&
                !bengkel.isEmpty() &&
                !kerusakan.isEmpty() &&
                !tanggal.isEmpty()
            ) {
                checkAdd = true
            }

            if (!checkAdd){return@setOnClickListener}

            val queue = Queue(0, noAntrian.toInt(), bengkel.toInt(), id, kerusakan, tanggal)

            val stringRequest: StringRequest = object : StringRequest(Method.POST, QueueApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val queue = gson.fromJson(response, Queue::class.java)

                    if(queue != null){
                        FancyToast.makeText(this@AddQueueActivity, "Berhasil tambah Queue", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
//                        sendNotification()

                        val bundle = Bundle()
                        bundle.putInt("id", id)
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
                            this@AddQueueActivity,
                            errors.getString("Error: message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Log.d("Error Regist", e.message.toString())
                        Toast.makeText(this@AddQueueActivity,e.message, Toast.LENGTH_LONG).show()
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
                    val requestBody = gson.toJson(queue)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

//                override fun getParams(): MutableMap<String, String>? {
//                    val params = HashMap<String, String>()
//                    //params["jumlahTiket"] = inputField?.text.toString()
//                    params["id_bengkel"] = bengkel
//                    params["id_user"] = id.toString()
//                    params["no_atrian"] = noAntrian
//                    params["kerusakan"] = kerusakan
//                    params["tanggal"] = tanggal
//                    return params
//
//                    val queue = Queue(0, noAntrian.toInt(), bengkel.toInt(), id, kerusakan, tanggal)
//                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queueReq!!.add(stringRequest)
        }
    }

//    fun setExposeDropDownMenu(){
//
//        queueReq= Volley.newRequestQueue(this)
//
//        val stringRequest: StringRequest = object : StringRequest(Method.GET, BengkelApi.GET_ALL_URL,
//            Response.Listener { response->
//                val gson = Gson()
//                val bengkelList: Array<Bengkel> = gson.fromJson(response,Array<Bengkel>::class.java)
//
//                if(bengkelList.isEmpty()){
//                    FancyToast.makeText(this, "Belum ada data Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
//                }else{
//                    val listNamaBengkel = arrayOf("")
//                    var i = 0
//                    for(bengkel in bengkelList){
//                        listNamaBengkel!![i] = bengkel.nama
//                        i++
//                    }
//                    val adapterBengkel: ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.item_list, listNamaBengkel)
//                    edBengkel!!.setAdapter(adapterBengkel)
//                }
//            }, Response.ErrorListener { error->
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    FancyToast.makeText(this, errors.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
//                }catch (e: Exception){
//                    Log.d("Error Login", e.message.toString())
//                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
//                }
//            }
//        ){
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String,String>()
//                headers["Accept"] = "application/json"
//                return headers
//            }
//        }
//        queueReq!!.add(stringRequest)
//    }
}