package com.example.muslim.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muslim.R
import com.example.muslim.adapter.DataModel
import com.example.muslim.adapter.display_quran.DataModelForDisplayQuran
import com.example.muslim.adapter.display_quran.RecyclerAdapterForDisplayQuran
import org.json.JSONObject
import java.io.BufferedReader

class ReadQuranActivity : AppCompatActivity() {

    private lateinit var soura_name: TextView
    private lateinit var soura_content: TextView
    private var id: Int = 0
    private var name: String? = null

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_quran)

        recyclerView = findViewById(R.id.read_recycler_view)


        soura_name = findViewById(R.id.sura_name)
        soura_content = findViewById(R.id.soura_content)

        id = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")

        soura_name.text = "$name"
        soura_content.text = ""

        var reader: BufferedReader
        var str = ""

        //اضافة بسم الله لباقي السور
        if (id != 1) {
            if (id != 9) {
                reader = application.assets.open("all_quran/0.txt").bufferedReader()
                str = reader.readLine()
                reader.close()
            }
        }

        val arrayList = ArrayList<DataModelForDisplayQuran>()
        arrayList.add(DataModelForDisplayQuran(str)) // to add besm ellah

        //fill array list to pass it to recycler view
        var count = 0
        str = ""
        reader = application.assets.open("all_quran/$id.txt").bufferedReader()
        reader.forEachLine {
            str += it
            count++
            if (count == 20){
                count = 0
                arrayList.add(DataModelForDisplayQuran(str))
                str = ""
            }
        }
        arrayList.add(DataModelForDisplayQuran(str))

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = RecyclerAdapterForDisplayQuran(arrayList)
    }
}
