package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.room.User
//import com.example.tubespbp.entity.Bengkel
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_bengkel.*
import kotlinx.android.synthetic.main.fragment_bengkel.view.*
import org.json.JSONObject
import server.api.BengkelApi
import server.api.UserApi
import server.models.Bengkel
import java.nio.charset.StandardCharsets

class FragmentBengkel(idLogin: Int) : Fragment() {
    private var queue: RequestQueue? = null
    private var idLogin = idLogin
    private var layoutLoading: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_bengkel, container, false)

        view.mapActivityBtn.setOnClickListener() {
            val intent= Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }

        view.addBengkelBtn.setOnClickListener() {
            isAdmin()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)

        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, BengkelApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val bengkelList: Array<Bengkel> = gson.fromJson(response,Array<Bengkel>::class.java)

                if(bengkelList.isEmpty()){
                    this@FragmentBengkel.context?.let { FancyToast.makeText(it, "Belum ada data Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show() }
                }else{
                    val adapter = activity?.let { RVBengkelAdapter(bengkelList, it, idLogin) }
                    val rvBengkel : RecyclerView = view.findViewById(R.id.rv_bengkel)
                    rvBengkel.layoutManager = layoutManager
                    rvBengkel.setHasFixedSize(true)
                    rvBengkel.adapter = adapter
                }
            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    this@FragmentBengkel.context?.let { FancyToast.makeText(it, errors.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                    this@FragmentBengkel.context?.let { FancyToast.makeText(it, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
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

    fun isAdmin(){
        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                for(user in userList){
                    if(user.id == idLogin){
                        if(user.username == "admin"){
                            val intent= Intent(activity, AddBengkelActivity::class.java)
                            intent.putExtra("id", idLogin)
                            startActivity(intent)
                        }else{
                            this@FragmentBengkel.context?.let { FancyToast.makeText(it, "Anda bukan admin, tidak bisa tambah Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show() }
                        }

                    }
                }

            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        context,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
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