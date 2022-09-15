package com.unho.unhomeals.data;

public class Meal {
    private String title;
    private String content;

    public Meal(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
