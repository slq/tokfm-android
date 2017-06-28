package com.slq.tokfm

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar

class DownloadPodcastOnClickListener(val activity: Activity) : AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("PodcastFragment", "Item clicked at position $position")

        val podcast = parent?.getItemAtPosition(position) as Podcast
        Log.d("PodcastFragment", "Item clicked $podcast")

        val permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            val REQUEST_EXTERNAL_STORAGE = 0
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_EXTERNAL_STORAGE
            )
        }

        val progressBar = view?.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.VISIBLE

        PodcastService.downlaod(view, podcast)
    }
}
