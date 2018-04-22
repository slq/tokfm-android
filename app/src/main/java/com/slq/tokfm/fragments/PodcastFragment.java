package com.slq.tokfm.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.slq.tokfm.PodcastParser;
import com.slq.tokfm.task.DownloadMorePodcastsAsyncTask;
import com.slq.tokfm.task.DownloadPodcastAsyncTask;
import com.slq.tokfm.task.ListPodcastsAsyncTask;
import com.slq.tokfm.PodcastAdapter;
import com.slq.tokfm.PodcastService;
import com.slq.tokfm.R;
import com.slq.tokfm.model.Podcast;

import java.util.LinkedList;
import java.util.List;

public class PodcastFragment extends Fragment implements FragmentWithProgress<Podcast> {

    private final Gson gson = new Gson();
    private final PodcastParser parser = new PodcastParser();
    private final PodcastService service = new PodcastService(gson, parser);

    private ListView listView;
    private SwipeRefreshLayout swipe;
    private PodcastFragment fragment;

    private List<Podcast> podcasts = new LinkedList<>();
    private boolean isDownloadRunning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragment = this;

        View view = inflater.inflate(R.layout.fragment_podcast, container, false);
        listView = view.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PodcastFragment", "List item clicked at position " + position);
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);


                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    int REQUEST_EXTERNAL_STORAGE = 0;
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }

                Podcast podcast = (Podcast) parent.getItemAtPosition(position);
                new DownloadPodcastAsyncTask(view, fragment, service).execute(podcast);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount != 0
                        && !isDownloadRunning) {
                    Log.d("Scroll Listener", "Trying to load more podcasts (current count $totalItemCount)");
                    isDownloadRunning = true;
                    new DownloadMorePodcastsAsyncTask(service, fragment).execute(totalItemCount);
                }
            }
        });

        swipe = view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ListPodcastsAsyncTask(fragment, service).execute();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        new ListPodcastsAsyncTask(fragment, service).execute();
    }

    @Override
    public void progress(List<Podcast> podcasts) {
        this.podcasts.addAll(podcasts);
        PodcastAdapter adapter = new PodcastAdapter(getContext(), this.podcasts);
        listView.setAdapter(adapter);
        swipe.setRefreshing(false);
    }

    @Override
    public void loadMore(List<Podcast> podcasts) {
        listView.requestLayout();
        PodcastAdapter adapter = (PodcastAdapter) listView.getAdapter();
        this.podcasts.addAll(podcasts);
        adapter.notifyDataSetChanged();
        isDownloadRunning = false;
        Log.d("PodcastFragment", String.format("Loaded: %d podcasts more (total %d podcasts)", podcasts.size(), this.podcasts.size()));
    }
}
