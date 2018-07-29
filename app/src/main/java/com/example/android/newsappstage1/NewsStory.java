package com.example.android.newsappstage1;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class NewsStory {

    private String headline;
    private String date;
    private String category;
    private String url;
    private String author;

    public NewsStory(String headline, String date, String category, String url) {
        this.headline = headline;
        this.date = date;
        this.category = category;
        this.url = url;
    }

    public NewsStory(String headline, String date, String category, String url, String author) {
        this.headline = headline;
        this.date = date;
        this.category = category;
        this.url = url;
        this.author = author;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
