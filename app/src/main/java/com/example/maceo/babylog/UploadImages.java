package com.example.maceo.babylog;

import com.google.firebase.database.Exclude;

public class UploadImages {
    private String mName;
    private String mImageUrl;
    private String key;

    public UploadImages() {
        //empty constructor needed
    }

    public UploadImages(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        this.mName = name;
        this.mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
