package org.example.model;

public class Properties {
    private String textColor;
    private String barColor;
    private Integer barOpacity;
    private Integer screenShare;

    public Properties(String textColor, String barColor, Integer barOpacity, Integer screenShare) {
        this.textColor = textColor;
        this.barColor = barColor;
        this.barOpacity = barOpacity;
        this.screenShare = screenShare;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBarColor() {
        return barColor;
    }

    public Integer getBarOpacity() {
        return barOpacity;
    }

    public Integer getScreenShare() {
        return screenShare;
    }
}
