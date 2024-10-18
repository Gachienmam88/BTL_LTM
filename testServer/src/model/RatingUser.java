/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utils.MySQLConnection;

/**
 *
 * @author chipc
 */
public class RatingUser implements Serializable{
    private static final long serialVersionUID = 1L;
    private int user_id;
    private int match_id;
    private int rating;

    public RatingUser(int user_id, int match_id, int rating) {
        this.user_id = user_id;
        this.match_id = match_id;
        this.rating = rating;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public static boolean checkRatingUser(int user_id , int match_id) throws SQLException{
        Connection conn= new MySQLConnection().getConnection();
       String query = "SELECT * FROM bxh_user WHERE  user_id = ? AND match_id = ?";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, user_id);
       stmt.setInt(2, match_id);
       ResultSet rs = stmt.executeQuery();
       while(rs.next()){
          return true;
       }
       return false;
   }
    
        
    public static boolean  addRatingUser(int user_id , int match_id , int rating) throws SQLException{
        Connection conn= new MySQLConnection().getConnection();
       String query = "INSERT INTO bxh_user VALUES (?,?,?)";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1,user_id);
       stmt.setInt(2,match_id);
       stmt.setInt(3, rating);
       return stmt.executeUpdate()>0;
    }
    public static int getRatingUserByMatch(int user_id , int match_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "SELECT * FROM bxh_user WHERE  user_id = ? AND match_id = ?";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, user_id);
       stmt.setInt(2, match_id);
       ResultSet rs = stmt.executeQuery();
       if(rs.next()){
          return rs.getInt("rating");
       }
       return 0;
   }
}
