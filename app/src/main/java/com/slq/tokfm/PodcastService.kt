package com.slq.tokfm

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import com.google.gson.Gson
import org.jsoup.Jsoup
import java.io.File
import java.io.IOException
import java.lang.String.format
import java.math.BigInteger
import java.security.MessageDigest


object PodcastService {

    fun listPodcasts(pageNum :Int = 0): List<Podcast> {
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

    fun downlaod(view: View, podcast: Podcast) {
        Log.d("PodcastService", "Downloading started $podcast")

        val directory = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/TokFM")
        directory.mkdirs()

        val file = File(directory, podcast.targetFilename)
        val hexTime = currentTimeSecondsToHex()
        val audioName = podcast.podcast_audio
        val digest = digest(hexTime, audioName)
        val bigInt = BigInteger(1, digest)
        val mdp = bigInt.toString(16).toLowerCase().padStart(32, '0')
        val uri = format("http://storage.tuba.fm/podcast/%s/%s/%s", mdp, hexTime, audioName)

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        // Listen for if the audio file can't be prepared
        mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
                // ... react appropriately ...
                // The MediaPlayer has moved to the Error state, must be reset!
                return false
            }
        })
        // Attach to when audio file is prepared for playing
        mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer) {
                mediaPlayer.start()
            }
        })
        // Set the data source to the remote URL
        // Trigger an async preparation which will file listener when completed
        try {
            mediaPlayer.setDataSource(uri)
            mediaPlayer.prepareAsync() // might take long! (for buffering, etc)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

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

    class ProgressBarAnimation(private val progressBar: ProgressBar, private val from: Float, private val to: Float) : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }
    }

    fun loadMorePodcasts(podcastsCount: Int): List<Podcast> {
        val nextPageNum = podcastsCount.div(10) + 1
        return listPodcasts(nextPageNum)
    }

}
