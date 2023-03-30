package com.example.maceo.babylog.Model;

/**
 * Created by maceo on 4/3/18.
 */

public class BottleFeeding {
    private String dateAndTime;
    private int amtInOZ;
    private String bfNotes;


    public BottleFeeding(){
        //empty constructor
    }

    //rigth click on generate -> constructor
    public BottleFeeding(String dateAndTime, int amtInOZ, String bfNotes) {
        this.dateAndTime = dateAndTime;
        this.amtInOZ = amtInOZ;
        this.bfNotes = bfNotes;
    }

    //rigth click on generate -> GETTERS AND SETTERS
    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getAmtInOZ() {
        return amtInOZ;
    }

    public void setAmtInOZ(int amtInOZ) {
        this.amtInOZ = amtInOZ;
    }

    public String getBfNotes() {
        return bfNotes;
    }

    public void setBfNotes(String bfNotes) {
        this.bfNotes = bfNotes;
    }
}
