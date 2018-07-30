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

    /**
     * Constructor for a News Story
     * @param headline
     * @param date
     * @param category
     * @param url
     * @param author
     */
    public NewsStory(String headline, String date, String category, String url, String author) {
        this.headline = headline;
        this.date = date;
        this.category = category;
        this.url = url;
        this.author = author;
    }

    // Getters for the News Story strings.
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
