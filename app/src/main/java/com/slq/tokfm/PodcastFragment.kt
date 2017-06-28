package com.slq.tokfm


import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView

class PodcastFragment : Fragment(), FragmentWithProgress {

    var listView: ListView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    var podcasts: MutableList<Podcast> = arrayListOf()
    var downloadRunning = false

    override fun progress(podcastList: MutableList<Podcast>) {
        podcasts = podcastList
        val adapter = PodcastAdapter(context, podcasts)
        val fragment = this

        swipeRefresh?.isRefreshing = false

        listView?.adapter = adapter
        listView?.onItemClickListener = DownloadPodcastOnClickListener(activity)

        listView?.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                Log.d("Scroll Listener", "Scroll state changed! $scrollState")
            }

            override fun onScroll(absView: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
//                Log.d("Scroll Listener", "first visible item: $firstVisibleItem, visible count: $visibleItemCount, total count: $totalItemCount")
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
        listView?.requestLayout()
        val adapter = listView?.adapter as PodcastAdapter

        this.podcasts.addAll(podcasts)
        adapter.notifyDataSetChanged()
        downloadRunning = false

        Log.d("PodcastFragment", "Loaded ${podcasts.size} podcasts more (total ${this.podcasts.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_podcast, container, false)

        listView = view.findViewById(R.id.list) as ListView

        swipeRefresh = view.findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(context, R.color.swipe_color_1),
                ContextCompat.getColor(context, R.color.swipe_color_2),
                ContextCompat.getColor(context, R.color.swipe_color_3),
                ContextCompat.getColor(context, R.color.swipe_color_4))

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = this

        DownloadPodcastList(fragment).execute()

        swipeRefresh?.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                DownloadPodcastList(fragment).execute()
            }
        })
    }

}
