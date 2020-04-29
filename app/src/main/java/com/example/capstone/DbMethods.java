package com.example.capstone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbMethods {
    private static final String url = "jdbc:mysql://localhost:3306/foodfinder";
    private static final String user = "root";
    private static final String password = "FoodFinderRoot1";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public static void connect() throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }

    public static DishDetails getDishDetails(String dishId){

        DishDetails myDetails = new DishDetails();
        String query = "SELECT `dish`.`dishId`,\n" +
                "    `dish`.`dishName`,\n" +
                "    `dish`.`avgRating`,\n" +
                "    `dish`.`description`,\n" +
                "    `dish`.`calories`,\n" +
                "    `dish`.`price`,\n" +
                "    `dish`.`vegetarian`,\n" +
                "    `dish`.`vegan`,\n" +
                "    `dish`.`dairy`,\n" +
                "    `dish`.`nuts`\n" +
                "FROM `foodfinder`.`dish`" +
                "WHERE `dishId` = " + dishId + ";";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);



            while (rs.next()) {
                myDetails.setDishId(rs.getString("dishId"));

                myDetails.setDishName(rs.getString("dishName"));
                myDetails.setAvgReview(rs.getFloat("avgRating"));
                myDetails.setDescription(rs.getString("description"));
                myDetails.setCalories(rs.getInt("calories"));
                myDetails.setPrice(rs.getString("price"));

                myDetails.setVegetarian(rs.getBoolean("vegetarian"));
                myDetails.setVegan(rs.getBoolean("vegan"));
                myDetails.setDairy(rs.getBoolean("dairy"));
                myDetails.setNuts(rs.getBoolean("nuts"));
            }

        } catch (SQLException sqlEx) {
            myDetails = null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return myDetails;
    }

    public static ReviewDetails getReviews(String dishId){
        ReviewDetails myDetails = new ReviewDetails();
        String query = "SELECT `review`.`reviewId`,\n" +
                "    `review`.`title`,\n" +
                "    `review`.`content`,\n" +
                "    `review`.`rating`,\n" +
                "    `review`.`userId`,\n" +
                "    `review`.`dishId`,\n" +
                "FROM `foodfinder`.`review`" +
                "WHERE `dishId` = " + dishId + ";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);



            while (rs.next()) {
                myDetails.setReviewId(rs.getString("reviewId"));
                myDetails.setTitle(rs.getString("title"));
                myDetails.setContent(rs.getString("content"));

                myDetails.setRating(rs.getInt("rating"));
                myDetails.setUserId(rs.getString("userId"));
                myDetails.setDishId(rs.getString("dishId"));
            }

        } catch (SQLException sqlEx) {
            myDetails = null;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return myDetails;
    }
}
