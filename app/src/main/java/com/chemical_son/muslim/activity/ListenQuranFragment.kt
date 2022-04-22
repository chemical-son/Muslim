package com.chemical_son.muslim.activity

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chemical_son.muslim.R
import com.chemical_son.muslim.adapter.fehrisListenAdapter.DataModel
import com.chemical_son.muslim.adapter.fehrisListenAdapter.RecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject

class ListenQuranFragment : Fragment(R.layout.fragment_listen_quran) {

    private lateinit var recyclerView: RecyclerView

    private lateinit var mediaPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()

        recyclerView = view.findViewById(R.id.recycler_view)
        val list = ArrayList<DataModel>()

        val inputStreamAsString =
            requireActivity().application.assets.open("fehris.json").bufferedReader()
                .use { it.readText() }
        val fileAsJSONArray = JSONArray(inputStreamAsString)

        var temp: JSONObject
        for (i in 0 until fileAsJSONArray.length()) {
            temp = fileAsJSONArray.getJSONObject(i)

            list.add(DataModel(temp.getString("name"), temp.getInt("id")))
        }

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        var adapter = RecyclerAdapter(list)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if(mediaPlayer != null)
                    if(mediaPlayer.isPlaying)
                        mediaPlayer.stop()
                playAyaNo(position + 1)
            }

        })
    }

    private fun formatSuraNo(suraNo: Int): String {

        return when {
            suraNo < 10 -> "00$suraNo"
            suraNo in 10..99 -> "0$suraNo"
            else -> suraNo.toString()
        }
    }

    private fun playAyaNo(suraNo: Int) {

        if (suraNo > 144 || suraNo < 1)
            return

        val url = "https://server11.mp3quran.net/yasser/${formatSuraNo(suraNo)}.mp3"

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                start()
                Toast.makeText(context, "${getText(R.string.sura_no)} $suraNo", Toast.LENGTH_SHORT)
                    .show()
            }
            setOnCompletionListener {
                if (suraNo == 144)
                    release()
                playAyaNo(suraNo + 1)
            }
        }
    }
}