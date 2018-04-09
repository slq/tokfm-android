package com.slq.tokfm.model;

import java.util.Comparator;
import java.util.List;

public class PodcastContainer {

    private List<Podcast> schedule;

    public List<Podcast> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Podcast> schedule) {
        this.schedule = schedule;
        this.schedule.sort(new Comparator<Podcast>() {
            @Override
            public int compare(Podcast o1, Podcast o2) {
                return o1.compareTo(o2);
            }
        });
    }

    @Override
    public String toString() {
        return "PodcastContainer{" +
                "schedule=" + schedule +
                '}';
    }
}
