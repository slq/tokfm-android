package com.slq.tokfm;

import android.util.Log;

import com.slq.tokfm.model.Podcast;
import com.slq.tokfm.model.PodcastContainer;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PodcastParser {

    // TODO Kotlin candidate
    public PodcastContainer parse(Document body) {
        PodcastContainer podcasts = new PodcastContainer();
        for (Element li : body.select("li[class='tok-podcasts__podcast']")) {
            String id = li.select("button[class='tok-podcasts__button tok-podcasts__button--addtofav']").attr("data-id");
            String imageHref = li.select("div[class='tok-podcasts__item tok-podcasts__item--img']> img").attr("src");
            String dateTime = li.select("div[class='tok-podcasts__item tok-podcasts__item--name']> div[class='tok-podcasts__row tok-podcasts__row--audition-time'] > span:eq(0)").text();
            String length = li.select("div[class='tok-podcasts__item tok-podcasts__item--name']> div[class='tok-podcasts__row  time tok-podcasts__row--time'] > span:eq(0)").text();
            String series = li.select("div[class='tok-podcasts__item tok-podcasts__item--name']> div[class='tok-podcasts__row tok-podcasts__row--audition-time'] > span:eq(1) > a").text();
            String leader = li.select("div[class='tok-podcasts__item tok-podcasts__item--name']").select("a[class='tok-podcasts__element--name']").text();
            String title = li.select("div[class='tok-podcasts__item tok-podcasts__item--name']").select("div[class='tok-podcasts__row tok-podcasts__row--name'] > a").text();

            Podcast podcast = new Podcast(id, title, series, leader, dateTime, length, imageHref);
            podcasts.add(podcast);

            Log.i("Podcast parser", "Parsed podcast: " + podcast);
        }

        Log.i("Podcast parser", "Finished parsing: " + podcasts.size() + " podcasts");
        return podcasts;
    }
}
