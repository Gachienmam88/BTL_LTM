/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import utils.MySQLConnection;

/**
 *
 * @author chipc
 */
public class Match implements Serializable {

    private int id;
    private int room_id;
    private Timestamp startTime;
    private Timestamp endTime;
    private static final long serialVersionUID = 1L;

    public Match(int id, int room_id, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.room_id = room_id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public static boolean addMatch(Match match) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = new MySQLConnection().getConnection();
            String query = "INSERT INTO matchs VALUES (?,?,?,?)";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, match.getId());
            stmt.setInt(2, match.getRoom_id());
            stmt.setTimestamp(3, match.getStartTime());
            stmt.setTimestamp(4, match.getEndTime());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getNewId() throws SQLException {
        Connection connection = new MySQLConnection().getConnection();
        String query = "SELECT COUNT(*) FROM matchs";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            return rs.getInt(1) + 1;
        }
        return 0;
    }

    public static boolean updateMatchByRoom(int roomId) throws SQLException {
        Connection conn = new MySQLConnection().getConnection();
        String query = "UPDATE matchs SET room_id = ? WHERE room_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setNull(1, java.sql.Types.INTEGER);
        stmt.setInt(2, roomId);
        return stmt.executeUpdate() > 0;
    }
}
