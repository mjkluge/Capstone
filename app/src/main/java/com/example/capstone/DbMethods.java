package com.example.capstone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbMethods {
    private static final String url = "jdbc:mysql://localhost:3306/test";
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
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);



            while (rs.next()) {
                myDetails.setDishId(rs.getString(dishId));

                myDetails.setDishName(rs.getString(dishId));
                myDetails.setDescription(rs.getString(dishId));
                myDetails.setCalories(rs.getInt(dishId));
                myDetails.setPrice(rs.getString(dishId));

                myDetails.setVegetarian(rs.getBoolean(dishId));
                myDetails.setVegan(rs.getBoolean(dishId));
                myDetails.setDairyFree(rs.getBoolean(dishId));
                myDetails.setNutFree(rs.getBoolean(dishId));
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
