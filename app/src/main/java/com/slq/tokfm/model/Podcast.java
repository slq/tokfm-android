package com.slq.tokfm.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URL;

public class Podcast implements Comparable<Podcast>{
    private String id;
    private String title;
    private String series;
    private String leader;
    private String dateTime;
    private String length;
    private String imageUrl;
    private Bitmap image;

    public Podcast(String id, String title, String series, String leader, String dateTime, String length, String imageUrl) {
        this.id = id;
        this.title = title;
        this.series = series;
        this.leader = leader;
        this.dateTime = dateTime;
        this.length = length;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSeries() {
        return series;
    }

    public String getLeader() {
        return leader;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSafeDateTime() {
        if(dateTime == null || dateTime.length() == 0 || !dateTime.contains(":")) {
            return dateTime;
        }
        return dateTime.replace(":", "");
    }

    public String getLength() {
        return length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage(){
        if (image == null) {
            image = downloadImage();
        }

        return image;
    }

    private Bitmap downloadImage() {
        try {
            return BitmapFactory.decodeStream(new URL(imageUrl).openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTargetFilename() {
        String filename = String.format("%s - %s - %s.mp3", getSafeDateTime(), series, title);
        return filename.replaceAll("\\?", ".")
                .replaceAll("!", ".")
                .replaceAll(": ", " - ")
                .replaceAll(":", "-")
                .replaceAll("\"", "")
                .replaceAll("'", "")
                .replaceAll("\\.\\.", ".");
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
                ", title='" + title + '\'' +
                ", series='" + series + '\'' +
                ", leader='" + leader + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", length='" + length + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", image=" + image +
                '}';
    }
}
