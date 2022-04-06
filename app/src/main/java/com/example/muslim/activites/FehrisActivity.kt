package com.example.muslim.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muslim.R
import com.example.muslim.adapter.DataModel
import com.example.muslim.adapter.RecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject

class FehrisActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fehris)

        recyclerView = findViewById(R.id.recycler_view)
        var list = ArrayList<DataModel>()

        val inputStreamAsString = application.assets.open("fehris.json").bufferedReader().use{ it.readText() }
        val fileAsJSONArray = JSONArray(inputStreamAsString)

        var temp: JSONObject
        for (i in 0 until fileAsJSONArray.length()){
            temp = fileAsJSONArray.getJSONObject(i)

            list.add(DataModel(temp.getString("name"), temp.getInt("id")))
        }

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = RecyclerAdapter(list)
    }
}