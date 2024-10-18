/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import utils.MySQLConnection;
import java.sql.*;
/**
 *
 * @author chipc
 */
public class Card implements Serializable {
    private int id;
    private String name;
    private String value;
    private String image;
    private static final long serialVersionUID = 1L;
    public Card(int id, String name, String value, String image) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public static int getCardIdByImage(String image) throws SQLException{
        Connection conn = new MySQLConnection().getConnection();
        String query = "SELECT * FROM cards WHERE image = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, image);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            return rs.getInt("id");
        }
        return 0;
    }
    public static Card getCardById(int id) throws SQLException{
        Connection conn = new MySQLConnection().getConnection();
        String query = "SELECT * FROM cards WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            return new Card(id,rs.getString("name"),rs.getString("value"),rs.getString("image"));
        }
        return null;
    }
}
