package com.example.myhealthapplication.Newsfeed;

import java.util.ArrayList;

public class mainNews {

    String status;
    String totalResults;
    ArrayList<com.example.myhealthapplication.Newsfeed.modelClass> articles;

    public mainNews(String status, String totalResults, ArrayList<com.example.myhealthapplication.Newsfeed.modelClass> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<com.example.myhealthapplication.Newsfeed.modelClass> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<com.example.myhealthapplication.Newsfeed.modelClass> articles) {
        this.articles = articles;
    }


}
