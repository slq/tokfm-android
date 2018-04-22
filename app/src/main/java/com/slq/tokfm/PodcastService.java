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
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.emptyList;

public class PodcastService {

    private static final String URL = "http://audycje.tokfm.pl/getsch";
    private static final String PODCAST_LIST_URL = "http://audycje.tokfm.pl/js/podcasty?offset=%d&with_guests=false&with_leaders=false";
    private static final String PODCAST_DOWNLOAD_DETAILS_URL = "http://audycje.tokfm.pl/gets";

    private final Gson gson;
    private final PodcastParser podcastParser;

    public PodcastService(Gson gson, PodcastParser podcastParser) {
        this.gson = gson;
        this.podcastParser = podcastParser;
    }

    public List<Podcast> listPodcasts(int offset) {
        Log.i("Podcast service", "Starting listing...");

        Document body;
        String url = String.format(PODCAST_LIST_URL, offset);
        try {
            body = Jsoup.connect(url).ignoreContentType(true).get();
        } catch (IOException e) {
            Log.e("Podcast service", "Failed to list podcasts", e);
            return emptyList();
        }

        Log.i("Podcast service", "Listing body content");

        PodcastContainer podcasts = podcastParser.parse(body);

        Log.i("Podcast service", "Finished listing...");

        return podcasts.getSchedule();
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

    public List<Podcast> loadMorePodcasts(Integer podcastsCount) {
        int nextPageNum = podcastsCount / 10 + 1;
        Log.i("Podcast service", "Requesting more podcasts with offset: " + nextPageNum);
        return listPodcasts(nextPageNum);
    }
}
