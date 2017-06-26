package com.slq.tokfm

import com.slq.tokfm.api.Podcast

interface FragmentWithProgress {

    fun progress(podcasts: List<Podcast>)
}
