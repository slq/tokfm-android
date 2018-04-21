package com.slq.tokfm.task;

import android.os.AsyncTask;
import android.util.Log;

import com.slq.tokfm.PodcastService;
import com.slq.tokfm.fragments.PodcastFragment;
import com.slq.tokfm.model.Podcast;

import java.util.Arrays;
import java.util.List;

public class ListPodcastsAsyncTask extends AsyncTask<Void, Integer, List<Podcast>> {

    private final PodcastFragment fragment;
    private final PodcastService service;

    public ListPodcastsAsyncTask(PodcastFragment fragment, PodcastService service) {
        this.fragment = fragment;
        this.service = service;
    }

    @Override
    protected List<Podcast> doInBackground(Void... voids) {
        Log.i("List podcasts", "Starting listing...");
        publishProgress(0);

        List<Podcast> podcasts = service.listPodcasts(0);
        publishProgress(100);

        return podcasts;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.i("List podcasts", "Listing progress: " + Arrays.toString(values));
    }

    @Override
    protected void onPostExecute(List<Podcast> podcasts) {
        super.onPostExecute(podcasts);
        fragment.progress(podcasts);
        Log.i("List podcasts", "Finished listing with: " + podcasts.size() + " results");
    }
}
