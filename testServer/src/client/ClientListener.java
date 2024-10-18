package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.Player;
import model.Request;
import model.Response;
import model.RoomUser;
import server.ClientHandler;

public class ClientListener implements Runnable {

    private boolean running = true;
    private Socket socket;
    private ObjectInputStream input;
    private Response response;
    private Client client;
    // Hàng đợi cho các response liên quan đến request
    private BlockingQueue<Response> requestQueue = new LinkedBlockingQueue<>();

    // Hàng đợi cho các broadcast không liên quan đến request
    private BlockingQueue<Response> broadcastQueue = new LinkedBlockingQueue<>();
    private final Semaphore readSemaphore = new Semaphore(1);

    public ClientListener(Socket socket, Client client) {
        this.client = client;
        this.socket = socket;
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                readSemaphore.acquire(); // Chờ cho đến khi có quyền đọc
                Object obj = input.readObject();
                if (obj instanceof Response) {
                    Response response = (Response) obj;
                    handleResponse(response);
                }
                new Thread(() -> {
                    while (running) {
                        try {
                            Response response = broadcastQueue.take(); // Chặn nếu không có yêu cầu
                            if (response.getTag().equals("ALERT")) {
                                new AlertInformation((String) response.getPayload()).act();
                            } else if (response.getTag().equals("UPDATE_LOBBY")) {
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        
                                        if (client.lobbyView != null) {
                                            client.lobbyView.updateRooms();
                                        }
                                    } catch (ClassNotFoundException | IOException ex) {
                                        Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);

                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);

                                    }
                                });

                            } else if (response.getTag().equals("UPDATE_ROOM_UI")) {
                                if (this.client.roomView != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        try {
                                            this.client.roomView.updatePlayerPosition();
                                        } catch (ClassNotFoundException | IOException | SQLException ex) {
                                            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                                }
                            } else if (response.getTag().equals("RECEIVE_INVITE")) {
                                String data = (String) response.getPayload();
                                String[] word = data.split(" ");
                                int sendId = Integer.parseInt(word[1]);
                                int receiveId = Integer.parseInt(word[0]);
                                if (this.client.lobbyView != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        try {
                                            new InviteForm(this.client.lobbyView, true, this.client, receiveId, sendId);
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                                }
                            } else if (response.getTag().equals("START_GAME")) {
                                int match_id = (int) response.getPayload();
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        Response res = (Response) client.sendRequest(new Request("GET_ROOMUSER_BY_USER_ID", client.getPlayer().getId()));
                                        RoomUser ru = (RoomUser) res.getPayload();
                                        this.client.roomView.toggleCancelButton(ru.getPosition(), false);
                                    } catch (ClassNotFoundException | IOException ex) {
                                        Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    StarRoomView rv = (StarRoomView) this.client.startPanel;
                                    rv.startCountDown();

                                    // Dùng Timer để kiểm tra cho đến khi FlipCardPanel khác null
                                    Timer timer = new Timer(500, null);
                                    timer.addActionListener(e -> {
                                        if (this.client.FlipCardPanel != null) {
                                            FlipCardPanel fc = (FlipCardPanel) this.client.FlipCardPanel;
                                            fc.setMatchId(match_id);
                                            timer.stop();
                                        }
                                    });
                                    timer.setRepeats(true);
                                    timer.start();
                                });
                            } else if (response.getTag().equals("FLIP_UPDATE_UI")) {
                                String data = (String) response.getPayload();
                                SwingUtilities.invokeLater(() -> {
                                    if (this.client.FlipCardPanel != null) {
                                        String[] word = data.split(" ");
                                        String image = word[0];
                                        int index = Integer.parseInt(word[1]);
                                        FlipCardPanel fc = (FlipCardPanel) this.client.FlipCardPanel;
                                        fc.setHiddenCard(index, image);
                                    }
                                });
                            } else if (response.getTag().equals("CAN'T FLIP")) {
                                SwingUtilities.invokeLater(() -> {
                                    if (this.client.FlipCardPanel != null) {
                                        FlipCardPanel fc = (FlipCardPanel) this.client.FlipCardPanel;
                                        fc.disableAllCards();
                                    }
                                });
                            } else if (response.getTag().equals("UPDATE_CHAT")) {
                                SwingUtilities.invokeLater(() -> {
                                    if (this.client.roomView != null) {
                                        String message = (String) response.getPayload();
                                        client.roomView.updateChat(message);
                                    }
                                });
                            }

                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                            running = false;
                        }
                    }
                }).start();
//                new Thread(() -> {
//                    while (true) {
//                        try {
//                            Response res = requestQueue.take(); // Chặn nếu không có yêu cầu
//                            
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }).start();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                readSemaphore.release();
            }
        }
    }

    private boolean isBroadcast(Response response) {
        // Ví dụ: kiểm tra nếu tag của response là các giá trị broadcast
        return response.getTag().equals("ALERT") || response.getTag().equals("UPDATE_LOBBY") || response.getTag().equals("RECEIVE_INVITE") || response.getTag().equals("UPDATE_ROOM_UI") || response.getTag().equals("START_GAME") || response.getTag().equals("FLIP_UPDATE_UI") || response.getTag().equals("CAN'T FLIP") || response.getTag().equals("UPDATE_CHAT");
    }

    // Phương thức để xử lý các loại response khác nhau
    private void handleResponse(Response response) {
        try {
            if (isBroadcast(response)) {
                broadcastQueue.put(response);
            } else {
                // Nếu là phản hồi cho request, đẩy vào hàng đợi requestQueue
                requestQueue.put(response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Response getResponse() throws InterruptedException {
        return requestQueue.take();
    }
}
