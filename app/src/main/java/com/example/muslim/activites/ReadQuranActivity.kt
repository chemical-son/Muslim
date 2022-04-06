package com.example.muslim.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.muslim.R
import com.example.muslim.adapter.DataModel
import org.json.JSONArray
import org.json.JSONObject

class ReadQuranActivity : AppCompatActivity() {

    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_quran)

        textView = findViewById(R.id.read_data)
        val data = intent.getSerializableExtra("data") as DataModel
        textView.text = ""


        var inputStreamAsString = application.assets.open("quran.json").bufferedReader().use{ it.readText() }
        var fileAsJSONArray = JSONArray(inputStreamAsString)


        //اضافة بسم الله لباقي السور
        var temp: JSONObject = fileAsJSONArray.getJSONObject(0)
        if(data.id != 1){
            if (data.id != 9){
                var strBesmEllah = temp.getString("aya_text")
                textView.append("${strBesmEllah.subSequence(0, strBesmEllah.length-1)}\n")
            }
        }

        inputStreamAsString = application.assets.open("quran_as_souar.json").bufferedReader().use{ it.readText() }
        fileAsJSONArray = JSONArray(inputStreamAsString)

        textView.append(fileAsJSONArray.getJSONObject(data.id-1).getString("content"))


    }
}