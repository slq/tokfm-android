package com.slq.tokfm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


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
        val seriesName = view.findViewById(R.id.series_name) as TextView
        val podcastDescription = view.findViewById(R.id.podcast_description) as TextView
        val published = view.findViewById(R.id.published) as TextView
        val size = view.findViewById(R.id.size) as TextView
//        val podcastSeriesDescription = view.findViewById(R.id.podcast_series_description) as TextView
        val podcastImage = view.findViewById(R.id.podcast_image) as ImageView


        //sets the text for item name and item description from the current item object
        seriesName.text = currentItem.series_name
        podcastName.text = currentItem.podcast_name
        if(currentItem.podcast_description.isNotEmpty()) {
            podcastDescription.text = currentItem.podcast_description
        } else {
            podcastDescription.visibility = View.GONE
        }
        published.text = currentItem.podcast_emission_text
        size.text = currentItem.podcast_size_mb
//        podcastSeriesDescription.text = currentItem.series_description
        podcastImage.setImageBitmap(currentItem.podcast_image)

        // returns the view for the current row
        return view
    }

}