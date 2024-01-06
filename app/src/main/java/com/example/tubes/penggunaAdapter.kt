package com.example.tubes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class penggunaAdapter (private val dataList: List<pengguna>,) :
    RecyclerView.Adapter<penggunaAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewReportType: TextView = itemView.findViewById(R.id.textViewReportType)
        val textViewReport: TextView = itemView.findViewById(R.id.textViewReport)
        val btnDetail: Button = itemView.findViewById(R.id.btnDetail)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        // Set data to views
        holder.textViewReportType.text = "${data.laporan}"
        holder.textViewReport.text = "${data.laporan} berhasil terkirim"

//        holder.textViewDetail.setOnClickListener {
//            // Handle detail button click, you can navigate to a detail activity here
//            // and pass data if needed
//        }
        holder.btnDetail.setOnClickListener {
            val context = holder.itemView.context
            val pindah = Intent(context, editData::class.java)
            // Handle detail button click, you can navigate to a detail activity here
            // and pass data if needed

            context.startActivity(pindah)
        }

        holder.btnDelete.setOnClickListener {
            // Handle delete button click, you can implement deletion logic here
            // For example, show a confirmation dialog before deleting
            listener?.onDeleteClick(position)
        }

        holder.btnDetail.setOnClickListener {
            val context = holder.itemView.context
            val pindah = Intent(context, editData::class.java)
            pindah.putExtra("PENGGUNA_ID", data.Id) // Mengirim ID data ke detailActivity
            context.startActivity(pindah)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}