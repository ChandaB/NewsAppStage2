package com.example.android.newsappstage2;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class NewsStory {

    private String headline;
    private String date;
    private String category;
    private String url;
    private String author;
    private String storyImageURL;

    //Constructor for a news story element
    public NewsStory(String headline, String date, String category, String url, String author, String storyImageURL) {
        this.headline = headline;
        this.date = date;
        this.category = category;
        this.url = url;
        this.author = author;
        this.storyImageURL = storyImageURL;
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

    public String getStoryImageURL() {
        return storyImageURL;
    }
}
