package com.slq.tokfm;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.slq.tokfm.model.Podcast;
import com.slq.tokfm.model.PodcastContainer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PodcastService {
    
    private static final String URL = "http://audycje.tokfm.pl/getsch";
    private static final String PODCAST_DOWNLOAD_DETAILS_URL = "http://audycje.tokfm.pl/gets";

    private final Gson gson;

    public PodcastService(Gson gson) {
        this.gson = gson;
    }

    public List<Podcast> listPodcasts() {
        Log.i("Podcast service", "Starting listing...");

        String body = "<empty>";
        try {
            body = Jsoup.connect(URL).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            Log.e("Podcast service", "Failed to list podcasts", e);
        }

        Log.i("Podcast service", "Listing body content: " + body);

        PodcastContainer podcasts = gson.fromJson(body, PodcastContainer.class);
        PodcastContainer removedPodcasts = filterEmptyPodcasts(podcasts);
        PodcastContainer finalPodcasts = filterNonEmptyPodcasts(podcasts);

        Log.i("Podcast service", "Parsed podcasts: " + podcasts);
        Log.i("Podcast service", "Removed podcasts: " + removedPodcasts);
        Log.i("Podcast service", "Final podcasts: " + finalPodcasts);

        Log.i("Podcast service", "Finished listing...");

        return finalPodcasts.getSchedule();
    }

    private PodcastContainer filterEmptyPodcasts(PodcastContainer container) {
        List<Podcast> podcasts = new LinkedList<>();
        for (Podcast podcast : container.getSchedule()) {
            if (podcast.getId() == null) {
                podcasts.add(podcast);
            }
        }

        PodcastContainer newContainer = new PodcastContainer();
        newContainer.setSchedule(podcasts);
        return newContainer;
    }

    private PodcastContainer filterNonEmptyPodcasts(PodcastContainer container) {
        List<Podcast> podcasts = new LinkedList<>();
        for (Podcast podcast : container.getSchedule()) {
            if (podcast.getId() != null) {
                podcasts.add(podcast);
            }
        }

        PodcastContainer newContainer = new PodcastContainer();
        newContainer.setSchedule(podcasts);
        return newContainer;
    }

    public void downloadPodcast(View view, Podcast podcast) {
        Log.i("Podcast service", "Starting downloading of podcast: " + podcast + "...");

        PodcastDownloadDetailsRequest request = new PodcastDownloadDetailsRequest(podcast.getId());
        String json = gson.toJson(request);
        String body = "<empty>";
        try {
            body = Jsoup.connect(PODCAST_DOWNLOAD_DETAILS_URL)
                    .timeout(60000)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .requestBody(json)
                    .post()
                    .wholeText();
        } catch (IOException e) {
            Log.e("Podcast service", "Failed to list podcasts", e);
        }
        Log.i("Podcast service", "Downloaded podcast download details: " + body);

        PodcastDownloadDetailsResponse response = gson.fromJson(body, PodcastDownloadDetailsResponse.class);
        Log.i("Podcast service", "Podcast download details: " + response);

        File parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File directory = new File(parent, "TokFM");
        directory.mkdirs();

        Log.i("Podcast service", "Directory exists? " + parent.exists());
        Log.i("Podcast service", "Is directory? " + parent.isDirectory());
        Log.i("Podcast service", "Directory exists? " + directory.exists());
        Log.i("Podcast service", "Is directory? " + directory.isDirectory());

        final File file = new File(directory, podcast.getTargetFilename());

        Response.Listener<byte[]> responseListener = new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("Podcast service", "Trying to save response to: " + file.getAbsolutePath());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Podcast Service", "Download complete. File saved at " + file);
            }
        };

        ByteRequest byteRequest = new ByteRequest(response.getUrl(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(view.getContext(), new HurlStack());
        queue.add(byteRequest);

        Log.d("PodcastService", "Downloading queued at Volley: " + podcast);
    }
}
