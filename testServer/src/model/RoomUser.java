/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import utils.MySQLConnection;

/**
 *
 * @author chipc
 */
public class RoomUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id_room;
    private int id_user;
    private boolean is_ready;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public RoomUser(int id_room, int id_user, boolean is_ready ,int position ) {
        this.position= position;
        this.id_room = id_room;
        this.id_user = id_user;
        this.is_ready = is_ready;
    }
    
    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }
    public static RoomUser getRoomUserById (int userId , int roomId){
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM userroom WHERE user_id = ? AND room_id=?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idUser = rs.getInt("user_id");
                int idRoom= rs.getInt("room_id");
                boolean is_ready= rs.getBoolean("is_ready");
                int position = rs.getInt("position");
                return new RoomUser(idRoom , idUser, is_ready , position);
            }
         
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public boolean isIs_ready() {
        return is_ready;
    }

    public void setIs_ready(boolean is_ready) {
        this.is_ready = is_ready;
    }
    public static ArrayList<RoomUser> getAllRoomUser() throws SQLException{
        ArrayList<RoomUser> roomuser =new ArrayList<>();
        Connection connection =  new MySQLConnection().getConnection();
        String query = "SELECT * FROM userroom";
        Statement stmt = connection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        while(rs.next()){
            int user_id= rs.getInt("user_id");
            int room_id = rs.getInt("room_id");
            boolean is_ready = rs.getBoolean("is_ready");
            int position  = rs.getInt("position");
            roomuser.add(new RoomUser(room_id , user_id , is_ready, position));
        }
        return roomuser;
    }
    public static boolean addRoomUser(RoomUser roomuser){
            Connection connection = null;
    PreparedStatement stmt = null;
        try {
             connection = new MySQLConnection().getConnection();
            String query = "INSERT INTO userroom VALUES (?,?,?,?)";
             stmt = connection.prepareStatement(query);
            stmt.setInt(1, roomuser.getId_user());
            stmt.setInt(2, roomuser.getId_room());
            stmt.setBoolean(3, roomuser.isIs_ready());
            stmt.setInt(4, roomuser.getPosition());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteRoomUser(int userId , int roomId) throws SQLException{
          Connection connection = new MySQLConnection().getConnection();
            String query = "DELETE FROM  userroom WHERE user_id = ? AND room_id = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, roomId);
            return stmt.executeUpdate() > 0;
    }
     public static RoomUser checkUserInRoom(int userId) throws SQLException{
         Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT *  FROM  userroom WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                return new RoomUser(rs.getInt("room_id"),rs.getInt("user_id"),rs.getBoolean("is_ready"),rs.getInt("position"));
            }
            return null;
     }  
     public static int getQuanityInRoom(int roomId) throws SQLException{
          Connection connection = new MySQLConnection().getConnection();
          int count =0 ;
          String query = "SELECT * FROM userroom";
          Statement stmt = connection.createStatement();
          ResultSet rs = stmt.executeQuery(query);
          while(rs.next()){
              if(rs.getInt("room_id")==roomId){
                  count++;
              }
          }
          return count;
     }
     public static RoomUser getRoomUserByUserId (int userId){
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM userroom WHERE user_id = ? ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idUser = rs.getInt("user_id");
                int idRoom= rs.getInt("room_id");
                boolean is_ready= rs.getBoolean("is_ready");
                int position = rs.getInt("position");
                return new RoomUser(idRoom , idUser, is_ready , position);
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
     public static boolean  updateStatusUserRoom(RoomUser roomuser, boolean status ) throws SQLException{
         int userId = roomuser.getId_user();
         Connection connection =  new MySQLConnection().getConnection();
         String query = "UPDATE userroom SET is_ready = ? WHERE user_id =? ";
         PreparedStatement  stmt = connection.prepareStatement(query);
         stmt.setBoolean(1,status);
         stmt.setInt(2,userId);
         return stmt.executeUpdate()>0;
     }
     public static ArrayList<Integer> getAllRoomUserByRoomId(int roomId){
         try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM userroom WHERE room_id = ? ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Integer> list = new ArrayList<>();
            while (rs.next()) {
                int idUser = rs.getInt("user_id");
                int idRoom= rs.getInt("room_id");
                boolean is_ready= rs.getBoolean("is_ready");
                int position = rs.getInt("position");
                list.add(idUser);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
     }
     public static boolean checkFullReady(int roomid) throws SQLException{
         Connection conn = new MySQLConnection().getConnection();
         String query = "SELECT * FROM userroom WHERE room_id = ?";
         PreparedStatement stmt = conn.prepareStatement(query);
         stmt.setInt(1, roomid);
         ResultSet rs = stmt.executeQuery();
         while(rs.next()){
             if(rs.getBoolean("is_ready")==false){
                 return false;
             }
         }
         return true;
     }
}
