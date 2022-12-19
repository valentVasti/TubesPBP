package com.example.tubespbp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.room.User
import com.google.gson.Gson
import com.master.permissionhelper.PermissionHelper
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.json.JSONObject
import server.api.QueueApi
import server.api.UserApi
import server.models.Queue
import java.nio.charset.StandardCharsets

class FragmentSearch(usernameLogin: Int) : Fragment(R.layout.fragment_search) {

    val idLogin = usernameLogin
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_search, container, false)

        view.cameraBtn.setOnClickListener() {

            var permissionHelper = PermissionHelper(this, arrayOf(Manifest.permission.CAMERA), 100)
            permissionHelper.denied {
                if (it) {
                    permissionHelper?.openAppDetailsActivity()
                } else {
                    Log.d("Message:", "Permission denied")
                }
            }

            //Request all permission
            permissionHelper.requestAll {
                Log.d("Message:", "All permission granted")
                val intent= Intent(activity, CameraActivity::class.java)
                intent.putExtra("id", idLogin)
                startActivity(intent)
            }

            //Request individual permission
            permissionHelper.requestIndividual {
                Log.d("Message:", "Individual Permission Granted")
                val intent= Intent(activity, CameraActivity::class.java)
                intent.putExtra("id", idLogin)
                startActivity(intent)
            }
        }

        view.addQueueBtn.setOnClickListener(){
            val intent= Intent(activity, AddQueueActivity::class.java)
            intent.putExtra("id", idLogin)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)

        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                for(user in userList){
                    if(user.id == idLogin){
                        if(user.username == "admin"){
                            showAll(view, layoutManager)
                        }else{
                            showByUser(view, layoutManager)
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

    fun showAll(view: View, layoutManager: LinearLayoutManager){
        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, QueueApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val queueList: Array<Queue> = gson.fromJson(response,Array<Queue>::class.java)
                val queueListByUser = mutableListOf<Queue>()

                for(queue in queueList){
                    queueListByUser.add(queue)
                }

                if(queueListByUser.isNullOrEmpty()){
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, "Belum ada data Queue", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show() }
                }else{
                    val adapter = activity?.let { QueueRVAdapter(queueListByUser!!, it, idLogin) }
                    val rvQueue : RecyclerView = view.findViewById(R.id.rv_queue)
                    rvQueue.layoutManager = layoutManager
                    rvQueue.setHasFixedSize(true)
                    rvQueue.adapter = adapter
                }
            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, errors.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
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

    fun showByUser(view: View, layoutManager: LinearLayoutManager){
        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, QueueApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val queueList: Array<Queue> = gson.fromJson(response,Array<Queue>::class.java)
                val queueListByUser = mutableListOf<Queue>()

                for(queue in queueList){
                    if(queue.id_user == idLogin){
                        queueListByUser.add(queue)
                    }
                }

                if(queueListByUser.isNullOrEmpty()){
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, "Belum ada data Queue", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show() }
                }else{
                    val adapter = activity?.let { QueueRVAdapter(queueListByUser!!, it, idLogin) }
                    val rvQueue : RecyclerView = view.findViewById(R.id.rv_queue)
                    rvQueue.layoutManager = layoutManager
                    rvQueue.setHasFixedSize(true)
                    rvQueue.adapter = adapter
                }
            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, errors.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                    this@FragmentSearch.context?.let { FancyToast.makeText(it, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show() }
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