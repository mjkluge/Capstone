package com.example.capstone;

public class DishDetails {
    String dishId;
    String dishName;
    float avgReview;
    String description;
    int calories;
    String price;
    boolean vegetarian;
    boolean vegan;
    boolean dairyFree;
    boolean nutFree;
    boolean glutenFree;

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

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

    public boolean isDairyFree() {
        return dairyFree;
    }

    public boolean isNutFree() {
        return nutFree;
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

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public void setNutFree(boolean nutFree) {
        this.nutFree = nutFree;
    }
}
