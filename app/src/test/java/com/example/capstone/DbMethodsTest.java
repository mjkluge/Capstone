package com.example.capstone;

import org.junit.Test;

import static org.junit.Assert.*;

public class DbMethodsTest {

    @Test
    public void getDishDetails() {
        DishDetails dd = DbMethods.getDishDetails("1");
        assertEquals("This test dish is very bland.", dd.getDescription());
    }

    @Test
    public void getReviews() {
    }
}