package com.example.tubespbp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.entity.Bengkel

class RVBengkelAdapter(private val data: Array<Bengkel>) : RecyclerView.Adapter<RVBengkelAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_bengkel,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.ivBengkel.setImageResource(currentItem.image)
        holder.tvNama.text = currentItem.name
        holder.tvDetails.text = currentItem.alamat
        holder.tvdetail.text = "${currentItem.jenis}||${currentItem.jamBuka}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivBengkel: ImageView = itemView.findViewById(R.id.ivbengkel)
        val tvNama : TextView = itemView.findViewById(R.id.tv_nama)
        val tvDetails : TextView = itemView.findViewById(R.id.tv_details)
        val tvdetail : TextView = itemView.findViewById(R.id.tv_detail)
    }
}