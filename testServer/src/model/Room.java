package model;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import utils.MySQLConnection;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String roomName;
    private boolean is_start;
//    private LinkedHashMap<Room,Player[]> listRoom;
//    private Player[] players;
    public Room(int id, String roomName,boolean is_start ) {
//        this.listRoom=new LinkedHashMap<>();
//        this.players = players;
        this.id = id;
        this.roomName = roomName;
        this.is_start=is_start;
    }

    public boolean isIs_start() {
        return is_start;
    }

    public void setIs_start(boolean is_start) {
        this.is_start = is_start;
    }

//    public Player[] getPlayers() {
//        return players;
//    }
//
//    public void setPlayers(Player[] players) {
//        this.players = players;
//    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    // Create Room
    public static boolean createRoom(Room room) {
        Connection connection = null;
        PreparedStatement stmt=null;
        try {
             connection = new MySQLConnection().getConnection();
            String query = "INSERT INTO rooms VALUES (?,?,?)";
             stmt = connection.prepareStatement(query);
            stmt.setInt(1, room.getId());
            stmt.setString(2, room.getRoomName());
            stmt.setBoolean(3, room.isIs_start());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Read Room
    public static Room getRoomById(int id) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM rooms WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idRoom = rs.getInt("id");
                String name= rs.getString("room_name");
                boolean is_start= rs.getBoolean("is_start");
                return new Room(idRoom,name,is_start);
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //
    // Update Room
    public static boolean updateRoom(int id, String newRoomName) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "UPDATE rooms SET room_name = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newRoomName);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Room
    public static boolean deleteRoom(int id) {
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "DELETE FROM rooms WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Get  all roomst
    public static LinkedHashMap<Integer,ArrayList<Player>> getAllRooms() {
        LinkedHashMap<Integer,ArrayList<Player>> listRoom = new LinkedHashMap<>();
        try  {
            Connection connection = new MySQLConnection().getConnection();
            String query = "SELECT * FROM userroom";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
            int userId = rs.getInt("user_id");

            // In ra giá trị để kiểm tr
            // Lấy Room và Player từ ID
            Room room = Room.getRoomById(roomId);
            Player player = Player.getPlayerById(userId);

            // Kiểm tra nếu Room đã có trong listRoom
            if (listRoom.containsKey(room.getId())) {
                listRoom.get(room.getId()).add(player);
            } else {
                ArrayList<Player> players = new ArrayList<>();
                players.add(player);
                listRoom.put(room.getId(), players);
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return listRoom;
    }
    public static int getRandomRoomId() throws SQLException{
        Connection connection = new MySQLConnection().getConnection();
        ArrayList<Integer> ListId = new ArrayList<Integer>();
        String query = "SELECT * FROM rooms";
        Statement stmt = connection.createStatement();
        ResultSet rs =stmt.executeQuery(query);
        while(rs.next()){
            ListId.add(rs.getInt("id"));
        }
        for(int i=1;i<=10000;i++){
            if(!ListId.contains((Integer)i)){
                return i;
            }
        }
        return 0;
    }
    public static boolean updateStatusRoom(int idRoom , boolean status) throws SQLException{
        Connection connection = new MySQLConnection().getConnection();
            String query = "UPDATE rooms SET is_start = ? WHERE id = ?";
              PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setBoolean(1, status );
            stmt.setInt(2, idRoom);
            return stmt.executeUpdate() > 0;
    }
}