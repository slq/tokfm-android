package com.slq.tokfm.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URL;

public class Podcast implements Comparable<Podcast>{

    @SerializedName("podcast_id")
    private Long id;

    @SerializedName("podcast_name")
    private String name;

    @SerializedName("series_name")
    private String seriesName;

    @SerializedName("emission_time")
    private String time;

    @SerializedName("podcast_img")
    private String imageUrl;

    private Bitmap image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSafeTime() {
        return time != null ? time.replaceAll(":", "") : "-time-";
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage(){
        if (image == null) {
            image = downloadImage();
        }

        return image;
    }

    private Bitmap downloadImage() {
        try {
            return BitmapFactory.decodeStream(new URL(getImageUrl()).openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTargetFilename() {
        return String.format("%d - %s - %s - %s.mp3", id, getSafeTime(), seriesName, name);
    }

    @Override
    public int compareTo(@NonNull Podcast podcast) {
        if(id == null && podcast.id == null) return 0;
        if(id == null) return -1;
        if(podcast.id == null) return 1;
        return podcast.id.compareTo(id);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
