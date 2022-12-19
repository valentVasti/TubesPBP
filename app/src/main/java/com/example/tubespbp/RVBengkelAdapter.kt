package com.example.tubespbp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.room.User
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import server.api.BengkelApi
import server.api.UserApi
//import com.example.tubespbp.entity.Bengkel
import server.models.Bengkel
import java.nio.charset.StandardCharsets

class RVBengkelAdapter(private val data: Array<Bengkel>, context: Context, idLogin: Int) : RecyclerView.Adapter<RVBengkelAdapter.viewHolder>() {

    private val context: Context
    private var queue: RequestQueue? = null
    private val idLogin = idLogin

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_bengkel,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var currentItem = data[position]
        holder.ivBengkel.setImageResource(R.drawable.honda)
        holder.tvNama.text = currentItem.nama
        holder.tvDetails.text = currentItem.alamat
        holder.tvdetail.text = "${currentItem.jenis}||${currentItem.jamOperasional}"

        holder.itemBengkel.setOnClickListener{
            // pindah ke edit bengkel
            isAdminEdit(currentItem.id)
        }

        holder.deleteBtn.setOnClickListener{
            isAdminDelete(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivBengkel: ImageView = itemView.findViewById(R.id.ivbengkel)
        val tvNama : TextView = itemView.findViewById(R.id.tv_nama)
        val tvDetails : TextView = itemView.findViewById(R.id.tv_details)
        val tvdetail : TextView = itemView.findViewById(R.id.tv_detail)
        var itemBengkel : ConstraintLayout = itemView.findViewById(R.id.item_bengkel)
        var deleteBtn: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    fun deleteBengkel(id: Int){

        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest =
            object : StringRequest(Method.DELETE, BengkelApi.DELETE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val mahasiswa = gson.fromJson(response, Bengkel::class.java)
                    if (mahasiswa != null)
                        FancyToast.makeText(context, "Data Berhasil Dihapus", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            context,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = java.util.HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }

    fun getALlBengkel(): Array<Bengkel>{
        lateinit var bengkelData: Array<Bengkel>
        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, BengkelApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val bengkelList: Array<Bengkel> = gson.fromJson(response,Array<Bengkel>::class.java)

                if(bengkelList.isEmpty()){
                    FancyToast.makeText(context, "Belum ada data Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                }else{
                    FancyToast.makeText(context, "Berhasil mengambil data Bengkel", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                }
            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(context, errors.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
                    FancyToast.makeText(context, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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
        return bengkelData
    }

    fun isAdminDelete(id: Int){

        queue= Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                for(user in userList){
                    if(user.id == idLogin){
                        if(user.username == "admin"){
                            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                            builder.setMessage("Are you sure want to delete this item?")
                                .setPositiveButton("YES",object: DialogInterface.OnClickListener    {
                                    override fun onClick(dialogInterface: DialogInterface, i: Int){
                                        deleteBengkel(id)
                                        //bikin refresh biar lsg tampil yang baru
                                    }
                                })
                                .show()
                        }else{
                            FancyToast.makeText(context, "Anda bukan admin, tidak bisa hapus Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
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

    fun isAdminEdit(id: Int){

        queue= Volley.newRequestQueue(context)


        val stringRequest: StringRequest = object : StringRequest(Method.GET, UserApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()
                val userList: Array<User> = gson.fromJson(response,Array<User>::class.java)

                for(user in userList){
                    if(user.id == idLogin){
                        if(user.username == "admin"){
                            val intent= Intent(context, EditBengkelActivity::class.java)
                            intent.putExtra("idBengkel", id)
                            intent.putExtra("id", idLogin)
                            startActivity(context, intent, null)
                        }else{
                            FancyToast.makeText(context, "Anda bukan admin, tidak bisa Edit Bengkel", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
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