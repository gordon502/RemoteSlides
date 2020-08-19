package org.example.model;

public class NewsTicker {
    private int id;
    private String text;
    private int duration;

    public NewsTicker(int id, String text, int duration) {
        this.id = id;
        this.text = text;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
