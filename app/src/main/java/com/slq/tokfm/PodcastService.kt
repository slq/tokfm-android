package com.slq.tokfm

import android.util.Log
import com.google.gson.Gson
import org.jsoup.Jsoup

object PodcastService {
    fun listPodcasts() : List<Podcast> {
        val pageNum = 0
        val url = "http://audycje.tokfm.pl/podcasts?offset=$pageNum"
        Log.d("PODCAST", "URL: $url")
        val json = Jsoup.connect(url).ignoreContentType(true).execute().body()
        Log.d("PODCAST", "Json: $json")
        val gson = Gson()
        val container = gson.fromJson<PodcastContainer>(json, PodcastContainer::class.java)
        Log.d("PODCAST", "Records: $container")
        Log.d("PODCAST", "Podcasts: ${container.records}")
        return container.records
    }
}
