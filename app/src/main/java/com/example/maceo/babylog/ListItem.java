package com.example.maceo.babylog;

import com.google.firebase.database.Exclude;

public class ListItem {

    private String Title;
    private String Date;
    private String Time;
    private long inMillis;
    private Boolean OnOff;

    private String key;
    public ListItem() {}


    public ListItem(String Title, String Date, String Time, long Millis, Boolean onOff) {
        this.Title = Title;
        this.Date = Date;
        this.Time = Time;
        this.inMillis = Millis;

        this.OnOff = onOff;
    }

    public Boolean getOnOff() {
        return OnOff;
    }

    public void setOnOff(Boolean onOff) {
        this.OnOff = onOff;
    }

    public String getTime() {
        return Time;
    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return Date;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDate(String Date) {
        this.Date= Date;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public long getInMillis() {
        return inMillis;
    }

    public void setInMillis(long inMillis) {
        this.inMillis = inMillis;
    }
}
