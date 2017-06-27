package com.slq.tokfm

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.jsoup.Jsoup
import java.io.File
import java.lang.String.format
import java.math.BigInteger
import java.security.MessageDigest


object PodcastService {
    fun listPodcasts(): List<Podcast> {
        val pageNum = 0
        val url = "http://audycje.tokfm.pl/podcasts?offset=$pageNum"
        Log.d("PodcastService", "URL: $url")
        val json = Jsoup.connect(url).ignoreContentType(true).execute().body()
        Log.d("PodcastService", "Json: $json")
        val gson = Gson()
        val container = gson.fromJson<PodcastContainer>(json, PodcastContainer::class.java)
        Log.d("PodcastService", "Records: $container")
        Log.d("PodcastService", "Podcasts: ${container.records}")
        return container.records
    }

    fun downlaod(context: Context, podcast: Podcast) {
        Log.d("PodcastService", "Downloading started $podcast")

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "${podcast.podcast_id}.mp3")
        val hexTime = currentTimeSecondsToHex()
        val audioName = podcast.podcast_audio
        val digest = digest(hexTime, audioName)
        val bigInt = BigInteger(1, digest)
        val mdp = bigInt.toString(16).toLowerCase().padStart(32, '0')
        val uri = format("http://storage.tuba.fm/podcast/%s/%s/%s", mdp, hexTime, audioName)

        val responseListener = Response.Listener<ByteArray> {
            file.writeBytes(it)
            Log.d("Podcast Service", "Download complete. File saved at $file")
        }

        val request = ByteRequest(uri, responseListener)
        val queue = Volley.newRequestQueue(context, HurlStack());
        queue.add(request);

        Log.d("PodcastService", "Downloading queued at Volley $podcast")
    }

    private fun currentTimeSecondsToHex(): String {
        return format("%x", Math.floor(System.currentTimeMillis() / 1000.0).toInt()).toUpperCase()
    }

    private fun digest(n: String, audioName: String): ByteArray {
        val message = "MwbJdy3jUC2xChua/$audioName$n".toByteArray()
        val md5 = MessageDigest.getInstance("MD5")
        return md5.digest(message)
    }
}
