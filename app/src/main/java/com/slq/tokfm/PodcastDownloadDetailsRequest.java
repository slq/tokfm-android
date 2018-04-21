package com.slq.tokfm;

public class PodcastDownloadDetailsRequest {
    private String pid;
    private String st;

    public PodcastDownloadDetailsRequest(String id) {
        pid = id;
        st = "tokfm";
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }
}
