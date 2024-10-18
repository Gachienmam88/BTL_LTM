/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import utils.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
/**
 *
 * @author chipc
 */
public class MatchUser implements Serializable {
    private int match_id;
    private int user_id;
    private int card_id;
    private int pos;
    private  Timestamp time;
    private static final long serialVersionUID = 1L;

    public MatchUser(int match_id, int user_id, int card_id, int pos, Timestamp time) {
        this.match_id = match_id;
        this.user_id = user_id;
        this.card_id = card_id;
        this.pos = pos;
        this.time = time;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
   public static boolean addMatchUser(MatchUser mu) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "INSERT INTO match_user VALUES (?,?,?,?,?)";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1,mu.getMatch_id());
       stmt.setInt(2,mu.getUser_id());
       stmt.setInt(3, mu.getCard_id());
       stmt.setInt(4, mu.getPos());
       stmt.setTimestamp(5, mu.getTime());
       return stmt.executeUpdate()>0;
   } 
   public static int getPosCardForUser(int match_id , int user_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       int userPos = (RoomUser.getRoomUserByUserId(user_id).getPosition()-1)*3;
       String query = "SELECT * FROM match_user WHERE user_id = ? AND match_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, user_id);
       stmt.setInt(2, match_id);
       ResultSet rs = stmt.executeQuery();
       ArrayList<Integer> list = new ArrayList<>();
       while(rs.next()){
           list.add(rs.getInt("position"));
       }
       for(int i = userPos+1 ; i<userPos+4;i++){
           if(!list.contains(i)){
               return i;
           }
       }
       return 0;
   }
   public static ArrayList<MatchUser> getMatchUserByMatch(int match_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "SELECT * FROM match_user WHERE  match_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, match_id);
       ResultSet rs = stmt.executeQuery();
       ArrayList<MatchUser> list = new ArrayList<>();
       while(rs.next()){
           list.add(new MatchUser(rs.getInt("match_id"),rs.getInt("user_id"),rs.getInt("card_id"),rs.getInt("position"),rs.getTimestamp("time")));
       }
       return list;
   }
   public static int getQuantityCardForUser(int match_id , int user_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       int userPos = (RoomUser.getRoomUserByUserId(user_id).getPosition()-1)*3;
       String query = "SELECT * FROM match_user WHERE user_id = ? AND match_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, user_id);
       stmt.setInt(2, match_id);
       ResultSet rs = stmt.executeQuery();
       ArrayList<Integer> list = new ArrayList<>();
       while(rs.next()){
           list.add(rs.getInt("position"));
       }
       return list.size();
   }
   public static boolean deleteMatchUser(int match_id , int user_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "DELETE FROM match_user WHERE user_id = ? AND match_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1, user_id);
       stmt.setInt(2, match_id);
       return stmt.executeUpdate()>0;
   }
   public static TreeMap<Integer,ArrayList<MatchUser>> getDetailMatch(int match_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "SELECT * FROM match_user WHERE  match_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1,match_id);
       ResultSet rs = stmt.executeQuery();
       TreeMap<Integer,ArrayList<MatchUser>> list = new TreeMap<>();
       while(rs.next()){
           int userId = rs.getInt("user_id");
           if(list.containsKey(userId)){
               list.get(userId).add(new MatchUser(match_id,userId,rs.getInt("card_id"),rs.getInt("position"),rs.getTimestamp("time")));
           }else{
               ArrayList<MatchUser> listData = new ArrayList<>();
               listData.add(new MatchUser(match_id,userId,rs.getInt("card_id"),rs.getInt("position"),rs.getTimestamp("time")));
               list.put(rs.getInt("user_id"), listData);
           }
       }
       return list;
   }
   public static LinkedHashMap<Integer,ArrayList<MatchUser>> getMatchUserByUserId(int user_id) throws SQLException{
       Connection conn= new MySQLConnection().getConnection();
       String query = "SELECT * FROM match_user WHERE  user_id = ? ";
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setInt(1,user_id);
       ResultSet rs = stmt.executeQuery();
       LinkedHashMap<Integer,ArrayList<MatchUser>> list = new LinkedHashMap<Integer,ArrayList<MatchUser>>();
       while(rs.next()){
          int match_id = rs.getInt("match_id");
          if(list.containsKey(match_id)){
               list.get(match_id).add(new MatchUser(match_id,user_id,rs.getInt("card_id"),rs.getInt("position"),rs.getTimestamp("time")));
           }else{
               ArrayList<MatchUser> listData = new ArrayList<>();
               listData.add(new MatchUser(match_id,user_id,rs.getInt("card_id"),rs.getInt("position"),rs.getTimestamp("time")));
               list.put(match_id, listData);
           }
       }
       return list;
   }
}
