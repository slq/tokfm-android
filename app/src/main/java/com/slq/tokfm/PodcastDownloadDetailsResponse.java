package com.slq.tokfm;

public class PodcastDownloadDetailsResponse {
    private String url;
    private String tok;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTok() {
        return tok;
    }

    public void setTok(String tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "PodcastDownloadDetailsResponse{" +
                "url='" + url + '\'' +
                ", tok='" + tok + '\'' +
                '}';
    }
}
