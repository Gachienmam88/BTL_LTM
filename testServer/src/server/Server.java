package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import model.Player;
import model.Response;
import model.RoomUser;

public class Server {
    private static final int PORT = 5000;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket;
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                System.out.println("New client connected : "+clientSocket.getInetAddress().getHostAddress());
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  broadcast(String tag ,String username , String message) throws IOException, InterruptedException{
        for (ClientHandler client : clientHandlers) {
            if(client.getPlayer()!=null&&client.getPlayer().getUsername()!=username ){
                  client.sendMessage(tag , message);
            }
        }
    }
    public static void  broadcastUpdate(String message ) throws IOException{
        for (ClientHandler client : clientHandlers) { 
           client.addBroadcastToQueue(new Response(message,null));
        }
    }
    public static void broadcastStartGame(int roomId , String message , int match_id) throws SQLException, IOException{
        ArrayList<RoomUser> roomUser = RoomUser.getAllRoomUser();
        ArrayList<Integer> list = new ArrayList<>();
        for(RoomUser rs : roomUser){
            if(rs.getId_room()==roomId){
                list.add(rs.getId_user());
            }
        }
        for (ClientHandler client : clientHandlers) {           
            if(list.contains(client.getPlayer().getId())){
                client.addBroadcastToQueue(new Response(message,match_id));
            }
        }
    }
    public static void broadcastMessage( String tag , String message ,ArrayList<Integer> list) throws SQLException, IOException{
        for (ClientHandler client : clientHandlers) {           
            if(list.contains(client.getPlayer().getId())){
                client.addBroadcastToQueue(new Response(tag,message));
            }
        }
    }
    public static void broadcastFlipCardGame(int roomId , String message , int match_id ,String image , int index) throws SQLException, IOException{
        ArrayList<RoomUser> roomUser = RoomUser.getAllRoomUser();
        ArrayList<Integer> list = new ArrayList<>();
        for(RoomUser rs : roomUser){
            if(rs.getId_room()==roomId){
                list.add(rs.getId_user());
            }
        }
        for (ClientHandler client : clientHandlers) {           
            if(list.contains(client.getPlayer().getId())){
                client.addBroadcastToQueue(new Response(message , image+" "+String.valueOf(index)));
            }
        }
    }
    public static void broadcastPerson(String message ,int user_id) throws IOException{
        for (ClientHandler client : clientHandlers) {           
            if(client.getPlayer().getId()==user_id){
                client.addBroadcastToQueue(new Response(message,null));
            }
        }
    }
    public static void broadcastRoomUpdate(int roomId , String message ,int userId) throws SQLException, IOException{
        ArrayList<RoomUser> roomUser = RoomUser.getAllRoomUser();
        ArrayList<Integer> list = new ArrayList<>();
        for(RoomUser rs : roomUser){
            if(rs.getId_room()==roomId && rs.getId_user()!=userId){
                list.add(rs.getId_user());
            }
        }
        for (ClientHandler client : clientHandlers) {           
            if(list.contains(client.getPlayer().getId())){
                client.addBroadcastToQueue(new Response(message,null));
            }
        }
    }
    public static ArrayList<String> getUserOnline(int userId){
        ArrayList<String> list = new ArrayList<>();
        for (ClientHandler ch:clientHandlers){
            if(ch.getPlayer().getId()!=userId){
                list.add(ch.getPlayer().getUsername());
            }
        }
        return list;
    }
    public static Set<ClientHandler> getClient(){
        return clientHandlers;
    }
        
    public static void addClientHandler(ClientHandler a){
        clientHandlers.add(a);
    }
    public static void removeClientHandler(ClientHandler a){
        clientHandlers.remove(a);
    }
    
}