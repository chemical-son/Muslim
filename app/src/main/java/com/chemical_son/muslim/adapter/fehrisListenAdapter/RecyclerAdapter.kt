package com.chemical_son.muslim.adapter.fehrisListenAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chemical_son.muslim.R

class RecyclerAdapter(private var items: ArrayList<DataModel>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private lateinit var myListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.item_name)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(dataModel: DataModel) {
            name.text = dataModel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fehris_recycle_view_item, parent, false)
        return ViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
