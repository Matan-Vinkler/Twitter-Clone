package com.matan.twitterclone;

public class Tweet {
    private String username;
    private String displayName;
    private String tweet;
    private String publishTime;

    public Tweet(String username, String displayName, String tweet, String publishTime) {
        this.username = username;
        this.displayName = displayName;
        this.tweet = tweet;
        this.publishTime = publishTime;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTweet() {
        return this.tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
