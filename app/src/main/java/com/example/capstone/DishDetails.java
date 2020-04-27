package com.example.capstone;

public class DishDetails {
    String dishId;
    String dishName;
    float avgReview;
    String description;
    int calories;
    String price;
    boolean vegetarian, vegan, dairy, nuts;

    public DishDetails() {
    }

    public String getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public float getAvgReview() {
        return avgReview;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public String getPrice() {
        return price;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isDairy() {
        return dairy;
    }

    public boolean isNuts() {
        return nuts;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setAvgReview(float avgReview) {
        this.avgReview = avgReview;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public void setDairy(boolean dairy) {
        this.dairy = dairy;
    }

    public void setNuts(boolean nuts) {
        this.nuts = nuts;
    }
}
