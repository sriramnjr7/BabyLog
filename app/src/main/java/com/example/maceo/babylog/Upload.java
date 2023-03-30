package com.example.maceo.babylog;

import com.google.firebase.database.Exclude;

/**
 * Created by Danny on 3/29/18.
 */

public class Upload {
    private String mImageUrl;
    private String mKey;

    public Upload(String trim, String s){
        // empty constructor needed
    }

    public Upload(String imageUrl){
        /*if(name.trim().isEmpty()){
            name = "no name";
        }*/

/*
        mName = name;
*/
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    /*public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }*/

    public String getmImageUrl(){ return mImageUrl;}

    public void setmImageUrl(String imageUrl){ this.mImageUrl = imageUrl;}

}
