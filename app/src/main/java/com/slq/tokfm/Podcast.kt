package com.slq.tokfm

data class Podcast(val podcast_id: String,
                   val podcast_name: String,
                   val series_name: String,
                   val series_description: String,
                   val podcast_audio: String,
                   val podcast_emission_text: String,
                   val podcast_size: Long,
                   val user_name: String,
                   val targetFilename: String)
