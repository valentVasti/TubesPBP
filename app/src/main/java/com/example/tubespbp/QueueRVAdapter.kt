package com.example.tubespbp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.queue.Queue

class QueueRVAdapter(
    val context: Context,
    val queueClickDeleteInterface: QueueClickDeleteInterface,
    val queueClickInterface: QueueClickInterface
) :
    RecyclerView.Adapter<QueueRVAdapter.ViewHolder>() {

    private val allQueues = ArrayList<Queue>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idTV = itemView.findViewById<TextView>(R.id.tvId)
        val namaTV = itemView.findViewById<TextView>(R.id.tvNama)
        val jenisTV = itemView.findViewById<TextView>(R.id.tvJenis)
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.queue_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.idTV.setText(allQueues.get(position).ID)
        holder.namaTV.setText(allQueues.get(position).nama)
        holder.jenisTV.setText(allQueues.get(position).jenisKendaraan)

        holder.deleteIV.setOnClickListener {

            queueClickDeleteInterface.onDeleteIconClick(allQueues.get(position))
        }

        holder.itemView.setOnClickListener {

            queueClickInterface.onQueueClick(allQueues.get(position))
        }
    }

    override fun getItemCount(): Int {

        return allQueues.size
    }

    fun updateList(newList: List<Queue>) {

        allQueues.clear()

        allQueues.addAll(newList)

        notifyDataSetChanged()
    }
}


    interface QueueClickDeleteInterface {

        fun onDeleteIconClick(queue: Queue)
    }

    interface QueueClickInterface {

        fun onQueueClick(queue: Queue)
    }
