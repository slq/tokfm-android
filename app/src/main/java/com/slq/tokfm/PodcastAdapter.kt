package com.slq.tokfm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.slq.tokfm.api.Podcast


class PodcastAdapter(private val context: Context, private val items: List<Podcast>) : BaseAdapter() {

    override fun getCount() = items.size

    override fun getItem(position: Int): Podcast = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // inflate the layout for each list row
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.podcast_row, parent, false)
        return getView(position, view)
    }

    fun getView(position: Int, view: View): View {
        // get current item to be displayed
        val currentItem = getItem(position)

        // get the TextView for item name and item description
        val podcastName = view.findViewById(R.id.podcast_name) as TextView
        val podcastDescription = view.findViewById(R.id.podcast_description) as TextView

        //sets the text for item name and item description from the current item object
        podcastName.text = currentItem.name
        podcastDescription.text = currentItem.seriesName

        // returns the view for the current row
        return view
    }
}