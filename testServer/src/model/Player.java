package model;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.MySQLConnection;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    public Player(int id, String username , String password) {
        this.id = id;
        this.username = username;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword(){
        return this.password;
    }
    
    // Create Player
    public static boolean createPlayer(String username, String password) {
        try {
            Connection connection = new MySQLConnection().getConnection();
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read Player
    public static Player getPlayerById(int id) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Player(rs.getInt("id"), rs.getString("username"),rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update Player
    public static boolean updatePlayer(int id, String newUsername) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "UPDATE users SET username = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Player
    public static boolean deletePlayer(int id) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all players
    public static List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                players.add(new Player(rs.getInt("id"), rs.getString("username"),rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
    
    public static Player authenticate (String username, String password){
        try  {
           Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Player(rs.getInt("id"),rs.getString("username"),rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Player getPlayerByUsername(String username){
        try  {
           Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Player(rs.getInt("id"),rs.getString("username"),rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}