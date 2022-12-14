package com.example.tubespbp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.room.User
import com.google.gson.Gson
import com.lowagie.text.pdf.PdfWriter
import com.pspdfkit.annotations.FreeTextAnnotation
import com.pspdfkit.document.PdfDocument
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bengkel.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject
import server.api.UserApi
import java.nio.charset.StandardCharsets

class FragmentProfile(var idLogin: Int) : Fragment () {

    private var queue : RequestQueue? = null
    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        view.editBtn.setOnClickListener(){
            val intent= Intent(activity, EditProfileActivity::class.java)
            intent.putExtra("id", idLogin)
            startActivity(intent)
        }

        return view

    }

//    private fun createPdf(usernameLogin: String) {
//        queue = Volley.newRequestQueue(context)
//
//        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
//            Response.Listener { response->
//                val gson = Gson()
//                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)
//
//                for(user in userList){
//                    if(user.username == usernameLogin){
//                        val freeTextAnnotation = FreeTextAnnotation(
//                            0,
//                            RectF(228f, 1024f, 828f, 964f),
//                            "Username" + user.username
//                        )
//                        freeTextAnnotation.textSize = 40f
//                        val writer = PdfWriter(file)
//                        val pdfDocument = PdfDocument(writer)
//                        document.annotationProvider.addAnnotationToPage(titleFreeTextAnnotation)
//
//
//                        break
//                    }
//                }
//
//            }, Response.ErrorListener { error->
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        context,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }catch (e: Exception){
//                    Log.d("Error Login", e.message.toString())
//                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
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
//        queue!!.add(stringRequest)
//    }

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
                    if(user.id == idLogin){
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