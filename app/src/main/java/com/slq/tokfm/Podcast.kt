package com.slq.tokfm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

data class Podcast(val podcast_id: String,
                   val podcast_name: String,
                   val series_name: String,
                   val series_description: String,
                   val podcast_audio: String,
                   val podcast_emission_text: String,
                   val podcast_size: Long,
                   val user_name: String,
                   val podcast_img: String,
                   var podcast_image : Bitmap,
                   val targetFilename: String){

    fun downloadPodcastImage() {
        podcast_image = BitmapFactory.decodeStream(URL(podcast_img).openStream())
    }
}
