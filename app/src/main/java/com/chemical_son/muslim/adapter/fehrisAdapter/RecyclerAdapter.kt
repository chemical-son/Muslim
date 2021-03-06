package com.chemical_son.muslim.adapter.fehrisAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chemical_son.muslim.R
import com.chemical_son.muslim.activity.ReadQuranActivity

class RecyclerAdapter(private var items: ArrayList<DataModel>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById<TextView>(R.id.item_name)

        fun bind(dataModel: DataModel){
            name.text = dataModel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fehris_recycle_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            var intent = Intent(it.context, ReadQuranActivity::class.java)
            intent.putExtra("id", items[position].id)
            intent.putExtra("name", items[position].name)
            it.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}