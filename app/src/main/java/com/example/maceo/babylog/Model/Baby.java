package com.example.maceo.babylog.Model;

/**
 * Created by maceo on 3/18/18.
 */

public class Baby {

    private String babyName;
    private String babyBirthDate;
    private int babyWeight;
    private String babyGender;



    //empty constructor
    public Baby() {

    }



    // constructor
    public Baby (String babyName, String babyBirthDate,
                 int babyWeight, String babyGender) {

        this.babyName = babyName;
        this.babyBirthDate = babyBirthDate;
        this.babyWeight = babyWeight;
        this.babyGender = babyGender;

    }




    // created getters
    // right click then click on "generate"
    // then click on "Getter"
    // select the ones you need
    public String getBabyName() {
        return babyName;
    }

    public String getBabyBirthDate() {
        return babyBirthDate;
    }

    public int getBabyWeight() {
        return babyWeight;
    }

    public String getBabyGender() {
        return babyGender;
    }
}


