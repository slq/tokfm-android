package com.slq.tokfm

interface FragmentWithProgress {

    fun progress(podcasts: MutableList<Podcast>)
    fun loadMore(podcasts: MutableList<Podcast>)
}
