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
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.slq.tokfm.DownloadPodcastDetails;
import com.slq.tokfm.DownloadPodcastList;
import com.slq.tokfm.PodcastAdapter;
import com.slq.tokfm.PodcastService;
import com.slq.tokfm.R;
import com.slq.tokfm.model.Podcast;

import java.util.List;

public class PodcastFragment extends Fragment implements FragmentWithProgress<Podcast> {

    private final Gson gson = new Gson();
    private final PodcastService service = new PodcastService(gson);

    private ListView listView;
    private SwipeRefreshLayout swipe;
    private PodcastFragment fragment;

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
                new DownloadPodcastDetails(view, fragment, service).execute(podcast);
            }
        });

        swipe = view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadPodcastList(fragment, service).execute();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        new DownloadPodcastList(fragment, service).execute();
    }

    @Override
    public void progress(List<Podcast> podcasts) {
        PodcastAdapter adapter = new PodcastAdapter(getContext(), podcasts);
        listView.setAdapter(adapter);
    }
}
