package com.slq.tokfm

import android.os.AsyncTask
import android.util.Log

class DownloadMorePodcasts(val fragment: FragmentWithProgress) : AsyncTask<Int, Int, MutableList<Podcast>>() {

    override fun doInBackground(vararg params: Int?): MutableList<Podcast> {
        Log.d("AsyncTask", "Starting with params $params")
        publishProgress(0)
        Log.d("AsyncTask", "Before downloading")
        val morePodcasts = PodcastService.loadMorePodcasts(params[0]!!)
        Log.d("AsyncTask", "After downloading")
        publishProgress(100)
        return morePodcasts.toMutableList()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("AsyncTask", "Downloading progress: ${values[0]}")
    }

    override fun onPostExecute(result: MutableList<Podcast>?) {
        Log.d("AsyncTask", "Downloaded ${result?.size} podcasts")
        if (result != null) {
            fragment.loadMore(result)
        }
    }
}
