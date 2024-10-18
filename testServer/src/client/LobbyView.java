/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import model.Player;
import model.Request;
import model.Response;
import model.Room;
import model.RoomUser;

/**
 *
 * @author chipc
 */
public class LobbyView extends javax.swing.JFrame {

    /**
     * Creates new form LobbyView
     */
    private Client client;
    private Player player;

    public LobbyView(Client client, Player player) throws ClassNotFoundException, IOException, InterruptedException {
        this.client = client;
        this.player = player;
        this.setTitle("Lobby");
        initComponents();
        initializeRooms();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        HelloLabel = new javax.swing.JLabel();
        roomPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        createRoomButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        HelloLabel.setText("jLabel1");

        roomPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/tett.jpg"))); // NOI18N
        roomPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 420));

        logoutButton.setText("Đăng xuất");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        jButton2.setText("Xem lịch sử đấu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        createRoomButton.setText("Thêm phòng");
        createRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRoomButtonActionPerformed(evt);
            }
        });

        searchButton.setText("Tìm phòng");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(358, 358, 358)
                                .addComponent(HelloLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(createRoomButton)
                                .addGap(83, 83, 83)
                                .addComponent(jButton2)
                                .addGap(94, 94, 94)
                                .addComponent(searchButton)
                                .addGap(88, 88, 88)
                                .addComponent(logoutButton)))
                        .addGap(0, 120, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(roomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(HelloLabel)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoutButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(createRoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void handleLogout() throws IOException, InterruptedException {
    // Hiển thị hộp thoại xác nhận đăng xuất
    int result = JOptionPane.showConfirmDialog(
        null, 
        "Bạn có muốn đăng xuất?", 
        "Xác nhận đăng xuất", 
        JOptionPane.YES_NO_OPTION
    );
    
    // Kiểm tra người dùng chọn xác nhận hay hủy
    if (result == JOptionPane.YES_OPTION) {
        // Người dùng chọn xác nhận, thực hiện đăng xuất
        try {
            Response response = (Response) client.sendRequest(new Request("LOG_OUT", player.getUsername()));
            String message = (String) response.getPayload();
            
            // Hiển thị thông báo sau khi đăng xuất
            JOptionPane.showMessageDialog(null, message);
            
            // Xóa thông tin người chơi và chuyển sang màn hình đăng nhập
            client.setPlayer(null);
            LoginView loginView = new LoginView(client);
            close(); // Đóng cửa sổ hiện tại
            loginView.display(); // Hiển thị màn hình đăng nhập
        } catch (ClassNotFoundException | IOException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    } else if (result == JOptionPane.NO_OPTION) {
        // Người dùng chọn hủy, chỉ đóng hộp thoại
        dispose();
    }
}

    private void initializeRooms() throws ClassNotFoundException, IOException, InterruptedException {
        HelloLabel.setText("Xin chào " + this.player.getUsername());
        client.setLobbyView(this);
        roomPanel.removeAll();
        Response res = (Response) client.sendRequest(new Request("GET_ALL_ROOM", null));
        LinkedHashMap<Integer, ArrayList<Player>> rooms = (LinkedHashMap<Integer, ArrayList<Player>>) res.getPayload(); ///Can sua ngay cho nay 
        roomPanel.setLayout(new GridLayout(0, 2, 5, 5));

        for (Map.Entry<Integer, ArrayList<Player>> entry : rooms.entrySet()) {
            int roomId = entry.getKey();
            ArrayList<Player> list = entry.getValue();
            Response res1 = (Response) client.sendRequest(new Request("GET_ROOM_BY_ID", roomId));
            Room room = (Room) res1.getPayload();
            // Tạo nút với thông tin phòng
            JButton roomButton = new JButton(
    "<html>" +
        "<table width='100%'>" +
            // Dòng 1: Tên phòng bên trái, ID phòng bên phải
            "<tr>" +
                "<td align='left'>"+"Tên phòng : " + room.getRoomName() + "</td>" +
                "<td align='right'>"+"Mã phòng : " + room.getId() + "</td>" +
            "</tr>" +
            // Dòng 2: Số người chơi ở giữa
            "<tr>" +
                "<td colspan='2' align='center'>Số người : " + list.size() + "</td>" +
            "</tr>" +
        "</table>" +
    "</html>"
);
            roomButton.setPreferredSize(new java.awt.Dimension(this.getWidth() / 2 - 10, roomButton.getPreferredSize().height));
            roomButton.addActionListener(e -> {
                try {
                    Response response = (Response) client.sendRequest(new Request("JOIN_ROOMUSER", String.valueOf(client.getPlayer().getId()) + " " + String.valueOf(roomId)));
                    String message = (String) response.getPayload();
                    if (message.equals("OK")) {
                        client.setLobbyView(null);
//                        SwingUtilities.invokeLater(() -> {
                            try {
                                new RoomView(client, roomId);
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
//                        });

                        this.dispose();
                    } else if (message.equals("FULL")) {
                        JOptionPane.showMessageDialog(null, "Phòng này đã đầy, vui lòng vào phòng khác !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Game đã bắt đầu , vào cái lồn !");
                    }
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            });
            roomPanel.add(roomButton);
        }
    }

    public void close() {
        this.dispose();
    }
    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        try {
            handleLogout();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            this.dispose();
            DashBoard db = new DashBoard(client);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void createRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRoomButtonActionPerformed
        showAddRoomDialog();
    }//GEN-LAST:event_createRoomButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        JDialog dialog = new JDialog(this, "Tạo phòng mới", true); // true để tạo modal
        dialog.setSize(300, 150);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Nhập mã phòng : ");
        JTextField roomIdField = new JTextField(20);
        JButton searchButton = new JButton("Tìm phòng");
        dialog.add(label);
        dialog.add(roomIdField);
        dialog.add(searchButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        searchButton.addActionListener(e -> {
            String value = roomIdField.getText().trim();
            if (value.equals("")) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập số phòng !");
            } else {
                try {
                    Response res = (Response) client.sendRequest(new Request("SEARCH_ROOM_ID", value + " " + String.valueOf(client.getPlayer().getId())));
                    if (res.getPayload() instanceof String) {
                        String message = (String) res.getPayload();
                        if (message.equals("NOT_VALID")) {
                            JOptionPane.showMessageDialog(null, "Phòng bạn vừa nhập không hợp lệ!");
                        } else if (message.equals("FULL")) {
                            JOptionPane.showMessageDialog(null, "Phòng này đã đầy ,vui lòng thử lại sau !");
                        } else if (message.equals("GAME_START")) {
                            JOptionPane.showMessageDialog(null, "Phòng này đã bắt đầu chơi !");
                        }
                    } else if (res.getPayload() instanceof RoomUser) {
                        RoomUser ru = (RoomUser) res.getPayload();
                        client.setLobbyView(null);
//                        SwingUtilities.invokeLater(() -> {
                            try {
                                this.dispose();
                                dialog.dispose();
                                new RoomView(client, ru.getId_room());
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
//                        });
                    }

                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

            }
        });
        dialog.setVisible(true);
    }//GEN-LAST:event_searchButtonActionPerformed
//    public void setCreateRoomListener(ActionListener listener) {
//        createRoomButton.addActionListener(listener);
//    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Tạo phòng mới", true); // true để tạo modal
        dialog.setSize(300, 150);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Nhập tên phòng:");
        JTextField roomNameField = new JTextField(20);
        JButton createButton = new JButton("Tạo phòng");
        JButton exitButton = new JButton("Thoát");
        dialog.add(label);
        dialog.add(roomNameField);
        dialog.add(createButton);

        dialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình chính

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomName = roomNameField.getText().trim();
                if (!roomName.isEmpty()) {
                    try {
                        addRoom(roomName, dialog); // Thêm phòng mới
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    // Đóng modal
                } else {
                    JOptionPane.showMessageDialog(dialog, "Tên phòng không được để trống!");
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Đóng modal
            }
        });
        dialog.setVisible(true);
    }

    public void updateRooms() throws ClassNotFoundException, IOException, InterruptedException {
        HelloLabel.setText("Xin chào " + this.player.getUsername());
        Response res = (Response) client.sendRequest(new Request("GET_ALL_ROOM", null));
        LinkedHashMap<Integer, ArrayList<Player>> rooms = (LinkedHashMap<Integer, ArrayList<Player>>) res.getPayload(); ///Can sua ngay cho nay 
        roomPanel.removeAll();
        roomPanel.setLayout(new GridLayout(0, 2, 5, 5));

        for (Map.Entry<Integer, ArrayList<Player>> entry : rooms.entrySet()) {
            int roomId = entry.getKey();
            ArrayList<Player> list = entry.getValue();
            Response res1 = (Response) client.sendRequest(new Request("GET_ROOM_BY_ID", roomId));
            Room room = (Room) res1.getPayload();
            // Tạo nút với thông tin phòng
            JButton roomButton = new JButton("<html><center>" + room.getRoomName() + "<br/>Players: " + list.size() + "</center></html>");
            roomButton.setPreferredSize(new java.awt.Dimension(this.getWidth() / 2 - 10, roomButton.getPreferredSize().height));
            roomButton.addActionListener(e -> {
                try {
                    Response response = (Response) client.sendRequest(new Request("JOIN_ROOMUSER", String.valueOf(client.getPlayer().getId()) + " " + String.valueOf(roomId)));
                    String message = (String) response.getPayload();
                    if (message.equals("OK")) {
                        client.setLobbyView(null);
                        this.dispose();
//                        SwingUtilities.invokeLater(() -> {
                            try {
                                new RoomView(client, roomId);
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
//                        });

                    } else if (message.equals("FULL")) {
                        JOptionPane.showMessageDialog(null, "Phòng này đã đầy, vui lòng vào phòng khác !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Game đã bắt đầu ");
                    }
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            });
            roomPanel.add(roomButton);
        }
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    private void addRoom(String roomName, JDialog dialog) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        Response resi = (Response) client.sendRequest(new Request("GET_RANDOM_ROOM_ID", null));
        int idRoom = (int) resi.getPayload();
        Response responseAddRoom = (Response) client.sendRequest(new Request("ADD_ROOM", new Room(idRoom, roomName, false)));
        String message = (String) responseAddRoom.getPayload();
        if (!message.equals("OK")) {
            JOptionPane.showMessageDialog(null, "Tạo phòng thất bại, vui lòng thử lại sau!");
            return; // Ngừng thực hiện nếu không thành công
        }
        Response responseAddRoomUser = (Response) client.sendRequest(new Request("ADD_ROOMUSER", new RoomUser(idRoom, player.getId(), false, 1)));
        String message2 = (String) responseAddRoomUser.getPayload();
        while (checkRoomUserExist(client.getPlayer().getId(), idRoom) == null) {
            Thread.sleep(500);
        }
        if (message2.equals("OK")) {
//            JOptionPane.showMessageDialog(null, "Tạo phòng thành công !");
//            SwingUtilities.invokeLater(() -> {
                try {
                    RoomView rv = new RoomView(client, idRoom);
                    client.setLobbyView(null);
                    this.dispose();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
//            });
        } else {
            JOptionPane.showMessageDialog(null, "Tạo phòng thất bại , vui lòng thử lại sau  !");
        }
//        if (checkRoomUserExist(client.getPlayer().getId(), idRoom) == null) {
//            Thread.sleep(500);
//        }

    }

    public Room checkRoomExist(int id) throws ClassNotFoundException, IOException, InterruptedException {
        Response res = (Response) client.sendRequest(new Request("GET_ROOM_BY_ID", id));
        Room room = (Room) res.getPayload();
        return room;
    }

    public RoomUser checkRoomUserExist(int userId, int roomId) throws ClassNotFoundException, IOException, InterruptedException {
        Response res = (Response) client.sendRequest(new Request("GET_ROOMUSER_BY_ID", String.valueOf(userId) + " " + String.valueOf(roomId)));
        RoomUser roomuser = (RoomUser) res.getPayload();
        return roomuser;
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LobbyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//         
//        /* Create and display the form */
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HelloLabel;
    private javax.swing.JButton createRoomButton;
    private javax.swing.JButton jButton2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel roomPanel;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables

}