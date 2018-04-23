package com.slq.tokfm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.slq.tokfm.model.Podcast;

import java.util.List;

public class PodcastAdapter extends BaseAdapter {

    private final Context context;
    private final List<Podcast> items;

    public PodcastAdapter(Context context, List<Podcast> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.podcast_row, null);
        }

        Podcast podcast = (Podcast) getItem(position);

        TextView podcastName = view.findViewById(R.id.podcast_name);
        TextView seriesName = view.findViewById(R.id.series_name);
        TextView podcastDescription = view.findViewById(R.id.podcast_description);
        TextView published = view.findViewById(R.id.published);
        TextView size = view.findViewById(R.id.size);
        ImageView image = view.findViewById(R.id.podcast_image);

        podcastName.setText(podcast.getTitle());
        seriesName.setText(podcast.getSeries());
        podcastDescription.setText(podcast.getLeader());
        published.setText(podcast.getDateTime());
        size.setText(podcast.getLength());
//        image.setImageBitmap(podcast.getImage());

        return view;
    }
}
