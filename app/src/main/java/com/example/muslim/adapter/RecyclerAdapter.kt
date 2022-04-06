package com.example.muslim.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muslim.R
import com.example.muslim.activites.ReadQuranActivity

class RecyclerAdapter(private var items: ArrayList<DataModel>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.item_name)

        fun bind(dataModel: DataModel){
            name.text = dataModel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.main_recycle_view_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            var intent = Intent(it.context, ReadQuranActivity::class.java)
            intent.putExtra("data", items[position])
            it.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}