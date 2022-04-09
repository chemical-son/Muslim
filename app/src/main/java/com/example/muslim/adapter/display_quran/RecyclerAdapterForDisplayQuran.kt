package com.example.muslim.adapter.display_quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muslim.R

class RecyclerAdapterForDisplayQuran(private var items: ArrayList<DataModelForDisplayQuran>): RecyclerView.Adapter<RecyclerAdapterForDisplayQuran.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var content: TextView = itemView.findViewById(R.id.content)

        fun bind(dataForDisplayQuran: DataModelForDisplayQuran){
            content.text = dataForDisplayQuran.supContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.read_recycle_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            //
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}