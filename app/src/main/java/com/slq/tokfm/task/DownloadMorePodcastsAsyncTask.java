package com.slq.tokfm.task;

import android.os.AsyncTask;
import android.util.Log;

import com.slq.tokfm.PodcastService;
import com.slq.tokfm.fragments.PodcastFragment;
import com.slq.tokfm.model.Podcast;

import java.util.List;

public class DownloadMorePodcastsAsyncTask extends AsyncTask<Integer, Integer, List<Podcast>> {
    private PodcastService service;
    private PodcastFragment fragment;

    public DownloadMorePodcastsAsyncTask(PodcastService service, PodcastFragment fragment) {
        this.service = service;
        this.fragment = fragment;
    }

    @Override
    protected List<Podcast> doInBackground(Integer... params) {
        Log.d("AsyncTask", "Downloading more podcasts");
        publishProgress(0);
        List<Podcast> podcasts = service.loadMorePodcasts(params[0]);
        Log.d("AsyncTask", "After downloading");
        publishProgress(100);
        return podcasts;
    }

    @Override
    protected void onPostExecute(List<Podcast> podcasts) {
        Log.d("AsyncTask", "Downloaded: " + podcasts.size() + " more podcasts");
        fragment.loadMore(podcasts);
    }
}
