package com.example.tubespbp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.room.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import server.api.UserApi
import java.nio.charset.StandardCharsets

class FragmentProfile(var usernameLogin: String) : Fragment () {

    private var queue : RequestQueue? = null
    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(context)

        val tvUsername : TextView = view.findViewById(R.id.tvUsername)
        val tvEmail : TextView = view.findViewById(R.id.tvEmail)
        val tvPhone : TextView = view.findViewById(R.id.tvPhone)
        val tvBirth : TextView = view.findViewById(R.id.tvBirth)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                for(user in userList){
                    if(user.username == usernameLogin){
                        tvUsername.setText(user.username)
                        tvEmail.setText(user.email)
                        tvPhone.setText(user.phone)
                        tvBirth.setText(user.birth)
                        break
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