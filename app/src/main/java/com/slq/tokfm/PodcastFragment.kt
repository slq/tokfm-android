package com.slq.tokfm


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class PodcastFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_podcast, container, false)

        val podcasts = listOf(
                Podcast("Podcast1", "Long description 1"),
                Podcast("Podcast2", "Very long description 2")
        )

        val adapter = PodcastAdapter(context, podcasts)

        val itemsListView = view.findViewById(R.id.list_view) as ListView
        itemsListView.adapter = adapter

        return view
    }

}
