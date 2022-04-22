package com.chemical_son.muslim.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chemical_son.muslim.R
import com.chemical_son.muslim.adapter.fehrisAdapter.DataModel
import com.chemical_son.muslim.adapter.fehrisAdapter.RecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject

class QuranFragment: Fragment(R.layout.fragment_quran) {

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        val list = ArrayList<DataModel>()

        val inputStreamAsString = requireActivity().application.assets.open("fehris.json").bufferedReader().use{ it.readText() }
        val fileAsJSONArray = JSONArray(inputStreamAsString)

        var temp: JSONObject
        for (i in 0 until fileAsJSONArray.length()){
            temp = fileAsJSONArray.getJSONObject(i)

            list.add(DataModel(temp.getString("name"), temp.getInt("id")))
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RecyclerAdapter(list)
    }
}