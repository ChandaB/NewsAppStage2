package com.example.android.newsappstage1;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class NewsStory {

    private String headline;
    private String date;
    private String category;
    private String subcategory;
    private String url;
//    private String author;

    public NewsStory(String headline, String date, String category, String subcategory, String url) {
        this.headline = headline;
        this.date = date;
        this.category = category;
        this.subcategory = subcategory;
        this.url = url;
//        this.author = author;
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

    public String getSubcategory() {
        return subcategory;
    }

    public String getUrl() {
        return url;
    }

}
