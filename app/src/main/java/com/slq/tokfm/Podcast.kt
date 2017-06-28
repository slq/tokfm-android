package com.slq.tokfm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import java.util.regex.Pattern

data class Podcast(val podcast_id: String,
                   val podcast_name: String,
                   val podcast_description: String,
                   val series_name: String,
                   val series_description: String,
                   val podcast_audio: String,
                   val podcast_emission_text: String,
                   val podcast_size: Long,
                   val user_name: String,
                   val podcast_img: String,
                   var podcast_image: Bitmap) {

    val targetFilename: String
        get() = "${podcast_emission_text.normalize()} - ${series_name.normalize()} - ${podcast_name.normalize()}.mp3"

    val podcast_size_mb: String
        get() = "%.2f MB".format(podcast_size.div(1024.0).div(1024.0))

    fun downloadPodcastImage() {
        podcast_image = BitmapFactory.decodeStream(URL(podcast_img).openStream())
    }

    fun String.normalize(): String {
        val str = replaceUnwantedCharacters(this).trim()
        return when {
            str.length > 150 -> str.substring(0..150)
            else -> str
        }
    }

    private fun replaceUnwantedCharacters(str: String): String {
        var fixed = replaceButNotLast(str, "!", ".")
        fixed = replaceButNotLast(fixed, "?", ".")
        return fixed.replace(":".toRegex(), "")
                .replace("\'\'".toRegex(), "\'")
                .replace("\"".toRegex(), "\'")
                .replace("\r".toRegex(), "")
                .replace("\n".toRegex(), "")
                .replace("„".toRegex(), "")
                .replace("\\\\".toRegex(), "-")
                .replace("/".toRegex(), "-")
                .replace(">".toRegex(), ".")
                .replace("<".toRegex(), ".")
                .replace("\\|".toRegex(), ".")
    }

    private fun replaceButNotLast(str: String, from: String, to: String): String {
        var result = str
        if (!result.contains(from)) {
            return result
        }

        if (result.lastIndexOf(from) == result.length - 1) {
            result = result.substring(0, result.length - 1)
        }

        return result.replace(Pattern.quote(from).toRegex(), to)
    }
}
