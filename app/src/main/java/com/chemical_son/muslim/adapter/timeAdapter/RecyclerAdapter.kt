package com.chemical_son.muslim.adapter.timeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chemical_son.muslim.R


class RecyclerAdapter(private var items: ArrayList<DataModel>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.name)
        var time = itemView.findViewById<TextView>(R.id.time)

        fun bind(dataModel: DataModel){
            name.text = dataModel.name
            time.text = dataModel.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.time_structure, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            Toast.makeText(it.context, items[position].name + ' ' + items[position].time, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}