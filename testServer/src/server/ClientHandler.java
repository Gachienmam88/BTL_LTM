package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Match;
import model.Player;
import model.Request;
import model.Response;
import model.Room;
import model.RoomUser;
import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import model.Card;
import model.MatchUser;
import model.RatingUser;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Player player;
    private BlockingQueue<Response> broadcastQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Request> requestQueue = new LinkedBlockingDeque<>();
    private boolean running = true;
    public final Object key = new Object();

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;

    }

    public ObjectOutputStream getOut() {
        return this.out;
    }

    public void run() {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(() -> {
            while (running) {
                try {
                    Request request = requestQueue.take(); // Chặn nếu không có yêu cầu
                    handleRequest(request);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        new Thread(() -> {

            while (running) {
                try {
                    Request request = (Request) in.readObject();
                    if (request != null) {
                        requestQueue.put(request); // Thêm yêu cầu vào hàng đợi để xử lý sau
                    }

                } catch (SocketException ex) {
                    try {
                        handleClientDisconnect();
                    } catch (SQLException ex1) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (IOException ex1) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (InterruptedException ex1) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    running = false; // Dừng chạy khi client thoát
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        while (running) {
            try {
                Response broadcastResponse = broadcastQueue.take();  // Lấy broadcast từ hàng đợi
                synchronized (key) {
                    this.getOut().writeObject(broadcastResponse);
                    this.getOut().flush();
                }
            } catch (InterruptedException | IOException e) {
                running = false;
            }
        }
    }

    public void addBroadcastToQueue(Response response) {
        try {
            broadcastQueue.put(response);  // Thêm broadcast vào hàng đợi của client này
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleRequest(Request input) throws IOException, ClassNotFoundException, SQLException, InterruptedException {
        if (input.getTag().startsWith("LOGIN")) {
            String[] words = (String[]) input.getPayload();
            String username = words[0];
            String password = words[1];
            Player player = Player.authenticate(username, password);
            checkValidLogin(player);

        } else if (input.getTag().startsWith("LOG_OUT")) {
            String words = (String) input.getPayload();
            String username = words;
            Server.addClientHandler(this);
            synchronized (key) {
                out.writeObject(new Response("LOG_OUT", "Đăng xuất thành công !"));
                out.flush();
            }
            Server.removeClientHandler(this);
            Server.broadcast("ALERT",this.player.getUsername(),"Người dùng " + username + " disconnected!");
            this.player = null;
        } else if (input.getTag().equals("ADD_ROOM")) {
            if (Room.createRoom((Room) input.getPayload()) == true) {
                synchronized (key) {
                    out.writeObject(new Response("ADD_ROOM", "OK"));
                    out.flush();
                }
            } else {
                synchronized (key) {
                    out.writeObject(new Response("ADD_ROOM", " NOT OK"));
                    out.flush();
                }
            }
        } else if (input.getTag().equals("ADD_ROOMUSER")) {
            if (RoomUser.addRoomUser((RoomUser) input.getPayload()) == true) {
                synchronized (key) {
                    out.writeObject(new Response("ADD_ROOMUSER", "OK"));
                    out.flush();
                }
                Server.broadcastUpdate("UPDATE_LOBBY");
            } else {
                synchronized (key) {
                    out.writeObject(new Response("ADD_ROOMUSER", "NOT OK"));
                    out.flush();
                }
            }
        } else if (input.getTag().equals("CHECK_VALID_JOIN_ROOM")) {
            int roomId = (int) input.getPayload();
            LinkedHashMap<Integer, ArrayList<Player>> listRoom = Room.getAllRooms();
            ArrayList<Player> listPlayer = listRoom.get((Integer) roomId);

        } else if (input.getTag().equals("JOIN_ROOMUSER")) {
            String words = (String) input.getPayload();
            String[] arrayWord = words.split(" ");
            int idPlayer = Integer.valueOf(arrayWord[0]);
            int idRoom = Integer.valueOf(arrayWord[1]);
            int emptyPosition = getPosition(idRoom);
            Room room = Room.getRoomById(idRoom);
            if (emptyPosition == 0) {
                synchronized (key) {
                    out.writeObject(new Response("JOIN_ROOMUER", "FULL"));
                    out.flush();
                }
            } else if (room.isIs_start() == true) {
                synchronized (key) {
                    out.writeObject(new Response("JOIN_ROOMUER", "GAME_START"));
                    out.flush();
                }
            } else {

                RoomUser.addRoomUser(new RoomUser(idRoom, idPlayer, false, emptyPosition));
                synchronized (key) {
                    out.writeObject(new Response("JOIN_ROOMUSER", "OK"));
                    out.flush();
                }
                Server.broadcastRoomUpdate(idRoom, "UPDATE_ROOM_UI", idPlayer);
                Server.broadcastUpdate("UPDATE_LOBBY");
            }
        } else if (input.getTag().equals("GET_USER_ONLINE")) {
            int userId = (int) input.getPayload();
            ArrayList<String> list = Server.getUserOnline(userId);
            synchronized (key) {
                out.writeObject(new Response("GET_USER_ONLINE", list));
                out.flush();
            }
        } else if (input.getTag().equals("EXIT_ROOM")) {
            RoomUser roomuser = (RoomUser) input.getPayload();
            if (roomuser != null) {
                RoomUser.deleteRoomUser(player.getId(), roomuser.getId_room());
                if (RoomUser.getQuanityInRoom(roomuser.getId_room()) == 0) {
                    Match.updateMatchByRoom(roomuser.getId_room());
                    Room.deleteRoom(roomuser.getId_room());
                }
                Server.broadcastUpdate("UPDATE_LOBBY");
                Server.broadcastRoomUpdate(roomuser.getId_room(), "UPDATE_ROOM_UI", roomuser.getId_user());
                synchronized (key) {
                    out.writeObject(new Response("EXIT_ROOM", "OK"));
                    out.flush();
                }
            }
        } else if (input.getTag().equals("EXIT_ROOM_START")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int userId = Integer.valueOf(word[0]);
            int matchId = Integer.valueOf(word[1]);
            RoomUser ru = RoomUser.getRoomUserByUserId(userId);
            RoomUser.deleteRoomUser(player.getId(), ru.getId_room());
            if (RoomUser.getQuanityInRoom(ru.getId_room()) == 0) {
                Match.updateMatchByRoom(ru.getId_room());
                Room.deleteRoom(ru.getId_room());
            }
            boolean rs = MatchUser.deleteMatchUser(matchId, userId);
            Server.broadcastUpdate("UPDATE_LOBBY");
            Server.broadcastRoomUpdate(ru.getId_room(), "UPDATE_ROOM_UI", ru.getId_user());
            synchronized (key) {
                out.writeObject(new Response("EXIT_ROOM", "OK"));
                out.flush();
            }
        } else if (input.getTag().equals("SEND_INVITE")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int receiveId = Integer.valueOf(word[0]);
            int sendId = Integer.valueOf(word[1]);
            RoomUser ru = RoomUser.getRoomUserByUserId(sendId);
            Room room = Room.getRoomById(ru.getId_room());
            if (RoomUser.checkUserInRoom(receiveId) != null) {
                errorSendInvite(receiveId, ru.getId_room());
            } else if (room.isIs_start() == true) {
                synchronized (key) {
                    out.writeObject(new Response("SEND_INVITE", "GAME_START"));
                    out.flush();
                }
            } else {
                sendInvite(receiveId, sendId);
                synchronized (key) {
                    out.writeObject(new Response("SEND_INVITE", "OK"));
                    out.flush();
                }
            }

        } else if (input.getTag().equals("ACCEPT_INVITE")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int receiveId = Integer.valueOf(word[0]);
            int roomId = Integer.valueOf(word[1]);
            if (RoomUser.getQuanityInRoom(roomId) >= 4) {
                synchronized (key) {
                    out.writeObject(new Response("ACCEPT_INVITE", "FULL"));
                    out.flush();
                }
            } else {
                boolean is_add = RoomUser.addRoomUser(new RoomUser(roomId, receiveId, false, getPosition(roomId)));
                if (is_add) {
                    Server.broadcastUpdate("UPDATE_LOBBY");
                    Server.broadcastRoomUpdate(roomId, "UPDATE_ROOM_UI", receiveId);
                    synchronized (key) {
                        out.writeObject(new Response("ACCEPT_INVITE", "OK"));
                        out.flush();
                    }
                } else {
                    synchronized (key) {
                        out.writeObject(new Response("ACCEPT_INVITE", "OK"));
                        out.flush();
                    }
                }

            }
        } else if (input.getTag().equals("SEARCH_ROOM_ID")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            String checkData = checkValidInput(word[0]);
            int userId = Integer.valueOf(word[1]);
            if (checkData.equals("")) {
                synchronized (key) {
                    out.writeObject(new Response("SEARCH_ROOM_ID", "NOT_VALID"));
                    out.flush();
                }
            } else {
                int so = Integer.valueOf(checkData);

                //Chua xu li phong nay da bat dau choi : 
                //Chua xu li phong co mat khau : 
                handlerIdRoom(userId, so);
            }
        } else if (input.getTag().equals("CLIENT_READY")) {
            int userId = (int) input.getPayload();
            RoomUser ru = RoomUser.getRoomUserByUserId(userId);
            boolean rs = RoomUser.updateStatusUserRoom(ru, true);
            if (rs) {
                synchronized (key) {
                    out.writeObject(new Response("CLIENT_READY", "OK"));
                    out.flush();
                }
                Server.broadcastRoomUpdate(ru.getId_room(), "UPDATE_ROOM_UI", userId);
                if (RoomUser.checkFullReady(ru.getId_room())) {
                    Room.updateStatusRoom(ru.getId_room(), true);
                    Match match = new Match(Match.getNewId(), ru.getId_room(), new Timestamp(System.currentTimeMillis()), null);
                    boolean result = Match.addMatch(match);
                    if (result) {
                        Server.broadcastStartGame(ru.getId_room(), "START_GAME", match.getId());
                    }
                }
            } else {
                synchronized (key) {
                    out.writeObject(new Response("CLIENT_READY", "NOT_OK"));
                    out.flush();
                }
            }
        } else if (input.getTag().equals("CANCEL_READY")) {
            int userId = (int) input.getPayload();
            RoomUser ru = RoomUser.getRoomUserByUserId(userId);
            boolean rs = RoomUser.updateStatusUserRoom(ru, false);
            if (rs) {
                synchronized (key) {
                    out.writeObject(new Response("CANCEL_READY", "OK"));
                    out.flush();
                }
                Server.broadcastRoomUpdate(ru.getId_room(), "UPDATE_ROOM_UI", userId);
            } else {
                synchronized (key) {
                    out.writeObject(new Response("CANCEL_READY", "NOT_OK"));
                    out.flush();
                }
            }
        } else if (input.getTag().equals("FLIP_CARD")) {
            String data = (String) input.getPayload();
            String[] word = data.split(" ");
            int match_id = Integer.valueOf(word[0]);
            int user_id = Integer.valueOf(word[1]);
            String image = word[2];
            int index = Integer.valueOf(word[3]);
            int cardId = Card.getCardIdByImage(image);
            int pos = MatchUser.getPosCardForUser(match_id, user_id);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            boolean rs = MatchUser.addMatchUser(new MatchUser(match_id, user_id, cardId, pos, time));
            if (rs) {

                if (MatchUser.getQuantityCardForUser(match_id, user_id) == 3) {
                    Server.broadcastPerson("CAN'T FLIP", user_id);
                }
                synchronized (key) {
                    out.writeObject(new Response("FLIP_CARD", "OK"));
                    out.flush();
                }
                Server.broadcastRoomUpdate(RoomUser.getRoomUserByUserId(user_id).getId_room(), "UPDATE_ROOM_UI", user_id);
                Server.broadcastFlipCardGame(RoomUser.getRoomUserByUserId(user_id).getId_room(), "FLIP_UPDATE_UI", match_id, image, index);
            }
        } else if (input.getTag().equals("GET_ALL_ROOM")) {
            synchronized (key) {
                out.writeObject(new Response("GET_ALL_ROOM", Room.getAllRooms()));
                out.flush();
            }
        } else if (input.getTag().equals("GET_ROOM_BY_ID")) {
            int roomId = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_ROOM_BY_ID", Room.getRoomById(roomId)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_ROOMUSER_BY_ID")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int userId = Integer.parseInt(word[0]);
            int roomId = Integer.parseInt(word[1]);
            synchronized (key) {
                out.writeObject(new Response("GET_ROOMUSER_BY_ID", RoomUser.getRoomUserById(userId, roomId)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_USERNAME_BY_ID")) {
            String username = (String) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_USERNAME_BY_ID", Player.getPlayerByUsername(username)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_ALL_ROOMUSER")) {
            synchronized (key) {
                out.writeObject(new Response("GET_ALL_ROOMUSER", RoomUser.getAllRoomUser()));
                out.flush();
            }
        } else if (input.getTag().equals("GET_PLAYER_BY_ID")) {
            int userId = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_PLAYER_BY_ID", Player.getPlayerById(userId)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_ROOMUSER_BY_USER_ID")) {
            int userId = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_ROOMUSER_BY_USER_ID", RoomUser.getRoomUserByUserId(userId)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_MATCHUSER_BY_MATCH_ID")) {
            int matchId = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_MATCHUSER_BY_MATCH_ID", MatchUser.getMatchUserByMatch(matchId)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_RANDOM_ROOM_ID")) {
            synchronized (key) {
                out.writeObject(new Response("GET_RANDOM_ROOM_ID", Room.getRandomRoomId()));
                out.flush();
            }
        } else if (input.getTag().equals("GET_DETAIL_MATCH")) {
            int match_id = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_DETAIL_MATCH", MatchUser.getDetailMatch(match_id)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_CARD_BY_ID")) {
            int card_id = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_CARD_BY_ID", Card.getCardById(card_id)));
                out.flush();
            }
        } else if (input.getTag().equals("CANCEL_ROOM_START")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int roomId = Integer.valueOf(word[0]);
            int userId = Integer.valueOf(word[1]);
            boolean rs1 = Room.updateStatusRoom(roomId, false);
            boolean rs2 = RoomUser.updateStatusUserRoom(RoomUser.getRoomUserByUserId(userId), false);
            if (rs1 && rs2) {
                synchronized (key) {
                    out.writeObject(new Response("CANCEL_ROOM_START", "OK"));
                    out.flush();
                    Server.broadcastRoomUpdate(roomId, "UPDATE_ROOM_UI", userId);
                }
            } else {
                synchronized (key) {
                    out.writeObject(new Response("CANCEL_ROOM_START", " NOT OK"));
                    out.flush();
                }
            }

        } else if (input.getTag().equals("ADD_RATING_USER")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int user_id = Integer.valueOf(word[0]);
            int match_id = Integer.valueOf(word[1]);
            int rating = Integer.valueOf(word[2]);

            if(RatingUser.checkRatingUser(user_id, match_id)==false){
                RatingUser.addRatingUser(user_id, match_id, rating);
            }

                synchronized (key) {
                    out.writeObject(new Response("ADD_RATING_USER", "OK"));
                    out.flush();
                }
            } 
               
            
            
         else if (input.getTag().equals("GET_MATCHUSER_BY_USER_ID")) {
            int user_id = (int) input.getPayload();
            synchronized (key) {
                out.writeObject(new Response("GET_MATCHUSER_BY_USER_ID", MatchUser.getMatchUserByUserId(user_id)));
                out.flush();
            }
        } else if (input.getTag().equals("GET_RATINGUSER_BY_MATCH")) {
            String data = (String) input.getPayload();
            String word[] = data.split(" ");
            int user_id = Integer.valueOf(word[0]);
            int match_id = Integer.valueOf(word[1]);
            synchronized (key) {
                out.writeObject(new Response("GET_RATINGUSER_BY_MATCH", RatingUser.getRatingUserByMatch(user_id, match_id)));
                out.flush();
            }
        } else if (input.getTag().equals("SEND_MESSAGE")) {
            String data = (String) input.getPayload();
            String word[] = data.split("//");
            String message = word[0];
            String type = word[1];
            int send_id = Integer.valueOf(word[2]);
            Player player = Player.getPlayerById(send_id);
            RoomUser ru = RoomUser.getRoomUserByUserId(send_id);
            if (type.equals("ALL")) {
                ArrayList<Integer> list = RoomUser.getAllRoomUserByRoomId(ru.getId_room());
                synchronized (key) {
                    out.writeObject(new Response("SEND_MESSAGE", "OK"));
                    out.flush();
                }
                Server.broadcastMessage("UPDATE_CHAT", player.getUsername() + " (Tất cả) : " + message, list);
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                int receive_id = Player.getPlayerByUsername(type).getId();
                list.add(receive_id);
                list.add(send_id);
                synchronized (key) {
                    out.writeObject(new Response("SEND_MESSAGE", "OK"));
                    out.flush();
                }
                Server.broadcastMessage("UPDATE_CHAT", player.getUsername() + "(Gửi riêng cho bạn ) : " + message, list);
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void errorSendInvite(int receiveId, int roomId) throws SQLException, IOException {
        if (RoomUser.checkUserInRoom(receiveId) != null) {
            if (RoomUser.getRoomUserByUserId(receiveId).getId_room() == roomId) {
                synchronized (key) {
                    out.writeObject(new Response("SEND_INVITE", "DUPLICATE"));
                    out.flush();
                }
            } else {
                synchronized (key) {
                    out.writeObject(new Response("SEND_INVITE", "ANOTHER_ROOM"));
                    out.flush();
                }
            }
        }
    }

    public int getPosition(int roomId) throws SQLException {
        int pos[] = new int[5];
        ArrayList<RoomUser> list = RoomUser.getAllRoomUser();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId_room() == roomId) {
                pos[list.get(i).getPosition()] = 1;
            }
        }
        for (int i = 1; i <= 4; i++) {
            if (pos[i] != 1) {
                return i;
            }
        }
        return 0;
    }

    public void sendInvite(int receiveId, int sendId) throws IOException {
        Set<ClientHandler> clientHandlers = Server.getClient();
        for (ClientHandler ch : clientHandlers) {
            if (ch.getPlayer().getId() == receiveId) {
                ObjectOutputStream clientOut = ch.getOut();
                synchronized (key) {
                    clientOut.reset();// Lấy ObjectOutputStream của người nhận
                    clientOut.writeObject(new Response("RECEIVE_INVITE", String.valueOf(receiveId) + " " + String.valueOf(sendId)));
                    clientOut.flush();
                }
                break; // Dừng vòng lặp sau khi gửi
            }
        }
    }

    public void checkValidLogin(Player player) throws IOException, InterruptedException {
        Set<ClientHandler> clientHandlers = Server.getClient();
        for (ClientHandler ch : clientHandlers) {
            if (ch.getPlayer().getId() == player.getId()) {
                synchronized (key) {
                    out.writeObject(new Response("LOGIN", "INVALID_SESSION"));
                    out.flush();
                }
                return;
            }
        }
        synchronized (key) {
            out.writeObject(new Response("LOGIN", player));  // Gửi đối tượng Player về client
            out.flush();
        }
        if (player != null) {
            Server.addClientHandler(this);
            this.player = player;
            Server.broadcast("ALERT", this.player.getUsername(),"Người dùng " + this.player.getUsername() + " connected!");
        }
    }

    void sendMessage(String tag , String message) throws IOException, InterruptedException {
        broadcastQueue.put(new Response(tag,message));
    }

    private void handleClientDisconnect() throws SQLException, IOException, InterruptedException {

        //Xu li disconnected 
        if (this.player != null) {
            Server.broadcast("ALERT",this.player.getUsername(), this.player.getUsername()+" has been disconnected !");
            Server.removeClientHandler(this);
        }
        RoomUser roomuser = RoomUser.checkUserInRoom(player.getId());
        if (roomuser != null) {
            //thang nay dang o 1 phong nao do 
            //kiem tra xem no co dang trong tran nao khong : 
            RoomUser.deleteRoomUser(player.getId(), roomuser.getId_room());
            if (RoomUser.getQuanityInRoom(roomuser.getId_room()) == 0) {
                Match.updateMatchByRoom(roomuser.getId_room());
                Room.deleteRoom(roomuser.getId_room());
            }
            Server.broadcastUpdate("UPDATE_LOBBY");
            Server.broadcastRoomUpdate(roomuser.getId_room(), "UPDATE_ROOM_UI", this.player.getId());
        }
        this.player = null;
    }

    public String checkValidInput(String data) {
        if (!data.matches("\\d+")) {
            return "";
        } else {
            return data.replaceFirst("^0+", "");
        }
    }

    public void handlerIdRoom(int userId, int roomId) throws SQLException, IOException {
        Room room = Room.getRoomById(roomId);
        if (room == null) {
            synchronized (key) {
                out.writeObject(new Response("SEARCH_ROOM_ID", "NOT_VALID"));
                out.flush();
            }
            return;
        }
        int quantity = RoomUser.getQuanityInRoom(roomId);
        if (room.isIs_start() == true) {
            synchronized (key) {
                out.writeObject(new Response("SEARCH_ROOM_ID", "GAME_START"));
                out.flush();
            }
            return;
        }
        if (quantity < 4) {
            RoomUser ru = new RoomUser(roomId, userId, false, getPosition(roomId));
            RoomUser.addRoomUser(ru);
            Server.broadcastUpdate("UPDATE_LOBBY");
            Server.broadcastRoomUpdate(roomId, "UPDATE_ROOM_UI", userId);
            synchronized (key) {
                out.writeObject(new Response("SEARCH_ROOM_ID", ru));
                out.flush();
            }
        } else {
            synchronized (key) {
                out.writeObject(new Response("SEARCH_ROOM_ID", "FULL"));
                out.flush();
            }
        }
    }
}
