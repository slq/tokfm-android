package com.slq.tokfm


import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import java.net.URL

class PodcastFragment : Fragment(), FragmentWithProgress {

    override fun progress(podcasts: List<Podcast>) {
        val adapter = PodcastAdapter(context, podcasts)

        val itemsListView = view.findViewById(R.id.list_view) as ListView
        itemsListView.adapter = adapter
        itemsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.d("PodcastFragment", "Item clicked at position $position")

            val podcast = parent.getItemAtPosition(position) as Podcast
            Log.d("PodcastFragment", "Item clicked $podcast")

            
            PodcastService.downlaod(context, podcast)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_podcast, container, false)

        DownloadPodcastList(this).execute(URL("http://www.wp.pl"))

        return view
    }

}
