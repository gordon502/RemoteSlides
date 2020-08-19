package org.example.model;

public class Slide {
    private int id;
    private String name;
    private boolean image;
    private Integer duration;

    public Slide(int id, String name, boolean image, Integer duration) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
