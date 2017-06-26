package com.slq.tokfm

import android.os.AsyncTask
import android.util.Log
import com.slq.tokfm.api.Podcast
import com.slq.tokfm.internal.HttpPodcastInfoService
import java.net.URL

class DownloadPodcastList(val fragment: FragmentWithProgress) : AsyncTask<URL, Int, List<Podcast>>() {

    override fun doInBackground(vararg params: URL?): List<Podcast> {
        Log.d("AsyncTask", "Starting with params $params")
        val podcastInfoService = HttpPodcastInfoService()
        publishProgress(0)
        Log.d("AsyncTask", "Before downloading")
//        val podcasts = podcastInfoService.all
//        TimeUnit.SECONDS.sleep(10)
        val podcasts = listOf(Podcast2(), Podcast2(), Podcast2())
        Log.d("AsyncTask", "After downloading")
        publishProgress(100)
        return podcasts
    }

    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("AsyncTask", "Downloading progress: ${values[0]}")
    }

    override fun onPostExecute(result: List<Podcast>?) {
        Log.d("AsyncTask", "Downloaded ${result?.size} podcasts")
        if (result != null) {
            fragment.progress(result)
        }
    }
}