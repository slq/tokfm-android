package com.slq.tokfm


import android.app.Fragment
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar

class PodcastFragment : Fragment(), FragmentWithProgress {

    var podcasts: MutableList<Podcast> = arrayListOf()
    var downloadRunning = false

    override fun progress(podcastList: MutableList<Podcast>) {
        podcasts = podcastList
        val adapter = PodcastAdapter(context, podcasts)
        val fragment = this

        val itemsListView = view.findViewById(R.id.list_view) as ListView
        itemsListView.adapter = adapter
        itemsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.d("PodcastFragment", "Item clicked at position $position")

            val podcast = parent.getItemAtPosition(position) as Podcast
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

            val progressBar = view.findViewById(R.id.progressBar) as ProgressBar
            progressBar.visibility = View.VISIBLE

            PodcastService.downlaod(view, podcast)
        }

        itemsListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                Log.d("Scroll Listener", "Scroll state changed! $scrollState")
            }

            override fun onScroll(absView: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                Log.d("Scroll Listener", "first visible item: $firstVisibleItem, visible count: $visibleItemCount, total count: $totalItemCount")
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount != 0
                        && !downloadRunning) {
                    Log.d("Scroll Listener", "Trying to load more podcasts (current count $totalItemCount)")
                    downloadRunning = true
                    DownloadMorePodcasts(fragment).execute(totalItemCount)
                }
            }
        })
    }

    override fun loadMore(podcasts: MutableList<Podcast>) {
        val itemsListView = view.findViewById(R.id.list_view) as ListView
        itemsListView.requestLayout()
        val adapter = itemsListView.adapter as PodcastAdapter

        this.podcasts.addAll(podcasts)
        adapter.notifyDataSetChanged()
        downloadRunning = false

        Log.d("PodcastFragment", "Loaded ${podcasts.size} podcasts more (total ${this.podcasts.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_podcast, container, false)

        DownloadPodcastList(this).execute()

        return view
    }

}
