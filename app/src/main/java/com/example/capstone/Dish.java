package com.example.capstone;

class Dish {


    String name;

    String description;

    String entryId;
    String venueId;
    boolean filtered;

    public Dish(){
        filtered = true;
    }

    //details from our DB
    float avgReview;
    int calories;
    String price;
    boolean vegetarian;
    boolean vegan;
    boolean dairyFree;
    boolean nutFree;
    boolean glutenFree;
}
