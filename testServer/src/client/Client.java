package client;


import java.io.*;
import java.net.Socket;
import javax.swing.JPanel;
import model.Player;
import model.Request;
import model.Response;


public class Client {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    ClientListener client;
    public LobbyView lobbyView;
    public RoomView roomView;
    public JPanel FlipCardPanel;
    public JPanel startPanel;
    Player player;

    public void startClient(String serverAddress, int port) {
        try {
            Socket socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to server successfully.");
            client = new ClientListener(socket, this);
            Thread listeningThread = new Thread(client);
            listeningThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InitListRoom() {

    }

    public void setLobbyView(LobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    public Object sendRequest(Request request) throws ClassNotFoundException, IOException, InterruptedException {
        out.writeObject(request);
        out.flush();
        Response response = this.client.getResponse(); // Lấy phản hồi từ hàng đợi
        return response;
    }

//    public boolean checkValidJoinRoom(int idRoom) throws ClassNotFoundException, IOException {
////        Response response = (Response) this.sendRequest(new Request("CHECK_VALID_JOIN_ROOM",idRoom));
////            return (boolean) response.getPayload();
//        LinkedHashMap<Integer, ArrayList<Player>> listRoom = Room.getAllRooms();
//        ArrayList<Player> listPlayer = getListUser(listRoom, idRoom);
//        if (listPlayer.size() >= 4) {
//            JOptionPane.showMessageDialog(null, "Phòng này đã đủ người , vui lòng chơi phòng khác !");
//            return false;
//        }
//        Room room = Room.getRoomById(idRoom);
//        if (room.isIs_start() == true) {
//            JOptionPane.showMessageDialog(null, "Phòng này đã bắt đầu , vui lòng chơi phòng khác !");
//            return false;
//        }
//        return true;
//    }

//    public ArrayList<Player> getListUser(LinkedHashMap<Integer, ArrayList<Player>> listRoom, int roomId) {
//        for (Map.Entry<Integer, ArrayList<Player>> entry : listRoom.entrySet()) {
//            int room = entry.getKey();
//            if (room == roomId) {
//                return entry.getValue();
//            }
//        }
//        return null;
//    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
    
}
