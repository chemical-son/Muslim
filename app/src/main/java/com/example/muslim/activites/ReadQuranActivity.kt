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


        var inputStreamAsString: String
        var fileAsJSONObject: JSONObject


        //اضافة بسم الله لباقي السور
        if(data.id != 1){
            if (data.id != 9){
                inputStreamAsString = application.assets.open("all_quran/0.json").bufferedReader().use{ it.readText() }
                fileAsJSONObject = JSONObject(inputStreamAsString)

                val strBesmEllah = fileAsJSONObject.getString("content")
                textView.append("${strBesmEllah.subSequence(0, strBesmEllah.length-1)}\n")
            }
        }
//src/main/Assets/all_quran
        inputStreamAsString = application.assets.open("all_quran/${data.id}.json").bufferedReader().use{ it.readText() }
        fileAsJSONObject = JSONObject(inputStreamAsString)

        textView.append(fileAsJSONObject.getString("content"))


    }
}