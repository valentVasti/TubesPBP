package com.example.tubespbp

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import server.api.QueueApi
import server.api.UserApi
import server.models.Bengkel
import server.models.Queue
import java.nio.charset.StandardCharsets
import java.util.HashMap

class QueueRVAdapter(private val data: List<Queue>, context: Context, idLogin: Int) :
    RecyclerView.Adapter<QueueRVAdapter.ViewHolder>() {

    private val context = context
    private var queueReq: RequestQueue? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvNoAntrian = itemView.findViewById<TextView>(R.id.tvNoAntrian)
        val tvNamaBengkel = itemView.findViewById<TextView>(R.id.tvNamaBengkel)
        val tvKerusakan = itemView.findViewById<TextView>(R.id.tvKerusakan)
        val tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item_queue,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var currentItem = data[position]
        holder.tvNoAntrian.text = currentItem.no_antrian.toString()
        holder.tvKerusakan.text = currentItem.kerusakan
        holder.tvTanggal.text = currentItem.tanggal

        queueReq = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, BengkelApi.GET_BY_ID_URL + currentItem.id_bengkel,
            Response.Listener { response->
                val gson = Gson()
                val bengkelList: Array<Bengkel> = gson.fromJson(response,Array<Bengkel>::class.java)

                holder.tvNamaBengkel.text = bengkelList[0].nama

            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Log.d("Error Show Profile:", error.message!!)
                }catch (e: Exception){
                    Log.d("Error Login", e.message.toString())
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
        queueReq!!.add(stringRequest)

        holder.deleteBtn.setOnClickListener(){
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure want to delete this item?")
                .setPositiveButton("YES",object: DialogInterface.OnClickListener    {
                    override fun onClick(dialogInterface: DialogInterface, i: Int){
                        deleteQueue(currentItem.id)
                        //bikin refresh biar lsg tampil yang baru
                    }
                })
                .show()
        }
    }

    override fun getItemCount(): Int {

        return data.size
    }

    fun deleteQueue(idQueue: Int){
        queueReq= Volley.newRequestQueue(context)

        val stringRequest: StringRequest =
            object : StringRequest(Method.DELETE, QueueApi.DELETE_URL + idQueue,
                Response.Listener { response ->
                    val gson = Gson()
                    val queue = gson.fromJson(response, Queue::class.java)
                    if (queue != null)
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
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queueReq!!.add(stringRequest)
    }
}
