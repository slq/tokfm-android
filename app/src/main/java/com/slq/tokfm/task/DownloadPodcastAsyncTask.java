package com.slq.tokfm.task;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.slq.tokfm.PodcastService;
import com.slq.tokfm.fragments.PodcastFragment;
import com.slq.tokfm.model.Podcast;

import java.util.Arrays;
import java.util.List;

public class DownloadPodcastAsyncTask extends AsyncTask<Podcast, Integer, Void> {

    private View view;
    private final PodcastFragment fragment;
    private final PodcastService service;

    public DownloadPodcastAsyncTask(View view, PodcastFragment fragment, PodcastService service) {
        this.view = view;
        this.fragment = fragment;
        this.service = service;
    }

    @Override
    protected Void doInBackground(Podcast... podcasts) {
        publishProgress(0);
        service.downloadPodcast(view, podcasts[0]);
        publishProgress(100);
        return null;
    }
}
