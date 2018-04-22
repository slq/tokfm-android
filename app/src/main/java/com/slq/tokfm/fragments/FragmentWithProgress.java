package com.slq.tokfm.fragments;

import com.slq.tokfm.model.Podcast;

import java.util.List;

interface FragmentWithProgress<T> {

    void progress(List<T> items);
    void loadMore(List<T> podcasts);
}
