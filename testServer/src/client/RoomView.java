/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
public class RoomView extends javax.swing.JFrame {

    /**
     * Creates new form RoomView
     */
    Client client;
    LinkedHashMap<Integer, ArrayList<Player>> listRoom;
    JPanel[] playerPanels = new JPanel[4];
    JPanel[] originalPanels;
    JLabel[] imageUser = new JLabel[4];
    JLabel[] nameUser = new JLabel[4];
    JButton[] inviteButton = new JButton[4];
    JButton[] readyButton = new JButton[4];
    JPanel[] cardPanel = new JPanel[4];
    JLabel[] cardLabel = new JLabel[12];
    JButton[] chatButton = new JButton[4];
    int roomId;

    public RoomView(Client client, int RoomId) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        client.roomView = this;
        this.roomId = RoomId;
        this.client = client;
        initComponents();
        setUpUI();
    }

    public void setUpUI() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        this.setTitle("Game room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Hiển thị ID phòng
        roomIdLabel.setText("Phòng " + this.roomId);
        // Tạo các panel người chơi cho 4 vị tris
        playerPanels[0] = JUSER1;
        playerPanels[1] = JUSER2;
        playerPanels[2] = JUSER3;
        playerPanels[3] = JUSER4;
        imageUser[0] = userImage1;
        imageUser[1] = userImage2;
        imageUser[2] = userImage3;
        imageUser[3] = userImage4;
        nameUser[0] = userLabel1;
        nameUser[1] = userLabel2;
        nameUser[2] = userLabel3;
        nameUser[3] = userLabel4;
        inviteButton[0] = invite1;
        inviteButton[1] = invite2;
        inviteButton[2] = invite3;
        inviteButton[3] = invite4;
        readyButton[0] = readyButton1;
        readyButton[1] = readyButton2;
        readyButton[2] = readyButton3;
        readyButton[3] = readyButton4;
        cardPanel[0] = cardPanel1;
        cardPanel[1] = cardPanel2;
        cardPanel[2] = cardPanel3;
        cardPanel[3] = cardPanel4;
        cardLabel[0] = cardUser1;
        cardLabel[1] = cardUser2;
        cardLabel[2] = cardUser3;
        cardLabel[3] = cardUser4;
        cardLabel[4] = cardUser5;
        cardLabel[5] = cardUser6;
        cardLabel[6] = cardUser7;
        cardLabel[7] = cardUser8;
        cardLabel[8] = cardUser9;
        cardLabel[9] = cardUser10;
        cardLabel[10] = cardUser11;
        cardLabel[11] = cardUser12;
        chatButton[0] = chat1;
        chatButton[1] = chat2;
        chatButton[2] = chat3;
        chatButton[3] = chat4;
        hiddenReadyButton();
//        setStatusCardPanel(false);
        for (int i = 0; i < 4; i++) {
            chatButton[i].setMargin(new Insets(0, 0, 0, 0));
            chatButton[i].setBorder(BorderFactory.createEmptyBorder());
            chatButton[i].setVisible(false);
        }
        sendBtn.setMargin(new Insets(0, 0, 0, 0));
        sendBtn.setBorder(BorderFactory.createEmptyBorder());
        chatArea.setEditable(false);
        addStartPanel();
        for (int i = 0; i < 4; i++) {
            readyButton[i].addActionListener(e -> {
                try {
                    Response res = (Response) client.sendRequest(new Request("CANCEL_READY", client.getPlayer().getId()));
                    String message = (String) res.getPayload();
                    if (message.equals("OK")) {
                        StarRoomView startRoomPanel = (StarRoomView) this.client.startPanel;
                        startRoomPanel.displayStartButton();
                        updatePlayerPosition();
                    } else {
                        JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra, vui lòng thử lại sau ");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        hiddenCardLabel();

        updatePlayerPosition();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    //Get roomid
    public int getRoomId() {
        return this.roomId;
    }

    public void toggleCancelButton(int pos, boolean status) {
        readyButton[pos - 1].setEnabled(status);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void addCardPanel() {
        CenterPanel.removeAll();
        CenterPanel.setLayout(new BorderLayout());
        JPanel child = new FlipCardPanel(client, roomId);
        CenterPanel.add(child, BorderLayout.CENTER);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void removeMainPanel() {
        CenterPanel.removeAll();
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void playingGame() {
        CenterPanel.removeAll();
        CenterPanel.setLayout(new BorderLayout());
        JPanel child = new FlipCardPanel(client, roomId);
        CenterPanel.add(child, BorderLayout.CENTER);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void addStartPanel() {
        CenterPanel.removeAll();
        CenterPanel.setLayout(new BorderLayout());
        JPanel child = new StarRoomView(client);
        CenterPanel.add(child, BorderLayout.CENTER);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void setStatusCardPanel(boolean status) {
        CenterPanel.setVisible(status);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void hiddenCardLabel() {
        for (int i = 0; i < 12; i++) {
            cardLabel[i].setVisible(false);
            cardLabel[i].setIcon(null);
            cardLabel[i].setText("");
        }
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void displayCardLabel(int i, ImageIcon icon) {
        cardLabel[i - 1].setVisible(true);
        cardLabel[i - 1].setText("");
        cardLabel[i - 1].setIcon(icon);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    public void hiddenReadyButton() {
        for (int i = 0; i < 4; i++) {
            readyButton[i].setVisible(false);
        }
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ExitButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        JUSER3 = new javax.swing.JPanel();
        userImage3 = new javax.swing.JLabel();
        userLabel3 = new javax.swing.JLabel();
        invite3 = new javax.swing.JButton();
        readyButton3 = new javax.swing.JButton();
        chat3 = new javax.swing.JButton();
        JUSER2 = new javax.swing.JPanel();
        userImage2 = new javax.swing.JLabel();
        userLabel2 = new javax.swing.JLabel();
        invite2 = new javax.swing.JButton();
        readyButton2 = new javax.swing.JButton();
        chat2 = new javax.swing.JButton();
        JUSER1 = new javax.swing.JPanel();
        userImage1 = new javax.swing.JLabel();
        userLabel1 = new javax.swing.JLabel();
        invite1 = new javax.swing.JButton();
        readyButton1 = new javax.swing.JButton();
        chat1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        CenterPanel = new javax.swing.JPanel();
        JUSER4 = new javax.swing.JPanel();
        userImage4 = new javax.swing.JLabel();
        userLabel4 = new javax.swing.JLabel();
        invite4 = new javax.swing.JButton();
        readyButton4 = new javax.swing.JButton();
        chat4 = new javax.swing.JButton();
        cardPanel1 = new javax.swing.JPanel();
        cardUser3 = new javax.swing.JLabel();
        cardUser2 = new javax.swing.JLabel();
        cardUser1 = new javax.swing.JLabel();
        cardPanel4 = new javax.swing.JPanel();
        cardUser10 = new javax.swing.JLabel();
        cardUser11 = new javax.swing.JLabel();
        cardUser12 = new javax.swing.JLabel();
        cardPanel2 = new javax.swing.JPanel();
        cardUser4 = new javax.swing.JLabel();
        cardUser5 = new javax.swing.JLabel();
        cardUser6 = new javax.swing.JLabel();
        cardPanel3 = new javax.swing.JPanel();
        cardUser9 = new javax.swing.JLabel();
        cardUser8 = new javax.swing.JLabel();
        cardUser7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        fillArea = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();
        userCombo = new javax.swing.JComboBox<>();
        roomIdLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ExitButton.setText("Thoát");
        ExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitButtonActionPerformed(evt);
            }
        });

        userImage3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/tải xuống.png"))); // NOI18N

        userLabel3.setText("jLabel6");

        invite3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/add.png"))); // NOI18N
        invite3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invite3ActionPerformed(evt);
            }
        });

        readyButton3.setText("Hủy");

        chat3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/chat.png"))); // NOI18N
        chat3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JUSER3Layout = new javax.swing.GroupLayout(JUSER3);
        JUSER3.setLayout(JUSER3Layout);
        JUSER3Layout.setHorizontalGroup(
            JUSER3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER3Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(JUSER3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JUSER3Layout.createSequentialGroup()
                        .addGroup(JUSER3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JUSER3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(userLabel3))
                            .addGroup(JUSER3Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(readyButton3)))
                        .addGap(34, 34, 34))
                    .addGroup(JUSER3Layout.createSequentialGroup()
                        .addComponent(userImage3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(invite3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(chat3)
                .addGap(10, 10, 10))
        );
        JUSER3Layout.setVerticalGroup(
            JUSER3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JUSER3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chat3)
                    .addComponent(invite3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(readyButton3))
        );

        userImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/tải xuống.png"))); // NOI18N

        userLabel2.setText("jLabel8");

        invite2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/add.png"))); // NOI18N
        invite2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invite2ActionPerformed(evt);
            }
        });

        readyButton2.setText("Hủy");

        chat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/chat.png"))); // NOI18N
        chat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JUSER2Layout = new javax.swing.GroupLayout(JUSER2);
        JUSER2.setLayout(JUSER2Layout);
        JUSER2Layout.setHorizontalGroup(
            JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER2Layout.createSequentialGroup()
                .addGroup(JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JUSER2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(invite2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(readyButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JUSER2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(userLabel2))
                    .addGroup(JUSER2Layout.createSequentialGroup()
                        .addComponent(userImage2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chat2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JUSER2Layout.setVerticalGroup(
            JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JUSER2Layout.createSequentialGroup()
                        .addGroup(JUSER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userImage2)
                            .addComponent(chat2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(userLabel2))
                    .addGroup(JUSER2Layout.createSequentialGroup()
                        .addComponent(invite2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(readyButton2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        userImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/tải xuống.png"))); // NOI18N

        userLabel1.setText("jLabel7");

        invite1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/add.png"))); // NOI18N

        readyButton1.setText("Hủy");
        readyButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readyButton1ActionPerformed(evt);
            }
        });

        chat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/chat.png"))); // NOI18N
        chat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JUSER1Layout = new javax.swing.GroupLayout(JUSER1);
        JUSER1.setLayout(JUSER1Layout);
        JUSER1Layout.setHorizontalGroup(
            JUSER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(readyButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JUSER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JUSER1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(userLabel1))
                    .addGroup(JUSER1Layout.createSequentialGroup()
                        .addComponent(userImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(invite1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chat1)))
                .addContainerGap())
        );
        JUSER1Layout.setVerticalGroup(
            JUSER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER1Layout.createSequentialGroup()
                .addComponent(readyButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JUSER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER1Layout.createSequentialGroup()
                        .addComponent(invite1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))
                    .addGroup(JUSER1Layout.createSequentialGroup()
                        .addGroup(JUSER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userImage1)
                            .addComponent(chat1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(userLabel1))
        );

        javax.swing.GroupLayout CenterPanelLayout = new javax.swing.GroupLayout(CenterPanel);
        CenterPanel.setLayout(CenterPanelLayout);
        CenterPanelLayout.setHorizontalGroup(
            CenterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        CenterPanelLayout.setVerticalGroup(
            CenterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );

        userImage4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/tải xuống.png"))); // NOI18N

        userLabel4.setText("jLabel9");

        invite4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/add.png"))); // NOI18N
        invite4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invite4ActionPerformed(evt);
            }
        });

        readyButton4.setText("Hủy");

        chat4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/chat.png"))); // NOI18N
        chat4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JUSER4Layout = new javax.swing.GroupLayout(JUSER4);
        JUSER4.setLayout(JUSER4Layout);
        JUSER4Layout.setHorizontalGroup(
            JUSER4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chat4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JUSER4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userImage4)
                    .addGroup(JUSER4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(userLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JUSER4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(readyButton4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUSER4Layout.createSequentialGroup()
                        .addComponent(invite4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        JUSER4Layout.setVerticalGroup(
            JUSER4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUSER4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JUSER4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userImage4)
                    .addComponent(chat4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userLabel4))
            .addGroup(JUSER4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(invite4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(readyButton4)
                .addContainerGap())
        );

        cardUser3.setText("The 1");

        cardUser2.setText("The 2");

        cardUser1.setText("The 3");

        javax.swing.GroupLayout cardPanel1Layout = new javax.swing.GroupLayout(cardPanel1);
        cardPanel1.setLayout(cardPanel1Layout);
        cardPanel1Layout.setHorizontalGroup(
            cardPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel1Layout.createSequentialGroup()
                .addComponent(cardUser3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser1)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        cardPanel1Layout.setVerticalGroup(
            cardPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(cardPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardUser3)
                    .addComponent(cardUser2)
                    .addComponent(cardUser1))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        cardUser10.setText("The 1");

        cardUser11.setText("The 2 ");

        cardUser12.setText("The 3");

        javax.swing.GroupLayout cardPanel4Layout = new javax.swing.GroupLayout(cardPanel4);
        cardPanel4.setLayout(cardPanel4Layout);
        cardPanel4Layout.setHorizontalGroup(
            cardPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardUser10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardPanel4Layout.setVerticalGroup(
            cardPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(cardPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardUser10)
                    .addComponent(cardUser11)
                    .addComponent(cardUser12))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        cardUser4.setText("The 1");

        cardUser5.setText("The 2");

        cardUser6.setText("The 3");

        javax.swing.GroupLayout cardPanel2Layout = new javax.swing.GroupLayout(cardPanel2);
        cardPanel2.setLayout(cardPanel2Layout);
        cardPanel2Layout.setHorizontalGroup(
            cardPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(cardUser4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser6)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        cardPanel2Layout.setVerticalGroup(
            cardPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(cardPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardUser4)
                    .addComponent(cardUser5)
                    .addComponent(cardUser6))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        cardUser9.setText("The 1");

        cardUser8.setText("The 2");

        cardUser7.setText("The 3");

        javax.swing.GroupLayout cardPanel3Layout = new javax.swing.GroupLayout(cardPanel3);
        cardPanel3.setLayout(cardPanel3Layout);
        cardPanel3Layout.setHorizontalGroup(
            cardPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cardUser9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardUser7)
                .addGap(31, 31, 31))
        );
        cardPanel3Layout.setVerticalGroup(
            cardPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(cardPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardUser9)
                    .addComponent(cardUser8)
                    .addComponent(cardUser7))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        fillArea.setColumns(20);
        fillArea.setRows(5);
        jScrollPane2.setViewportView(fillArea);

        sendBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/send1.png"))); // NOI18N
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        userCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cardPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JUSER1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1067, 1067, 1067))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cardPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(JUSER3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(594, 594, 594))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(cardPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(JUSER4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(70, 70, 70)
                        .addComponent(CenterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(155, 155, 155)
                                .addComponent(jLabel1))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JUSER2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(cardPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(sendBtn))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(userCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(180, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(166, 166, 166)
                                .addComponent(JUSER4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(JUSER2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(userCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JUSER3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CenterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(JUSER1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(cardPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );

        roomIdLabel.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        roomIdLabel.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roomIdLabel)
                .addGap(827, 827, 827)
                .addComponent(ExitButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roomIdLabel)
                    .addComponent(ExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitButtonActionPerformed
        try {
            Response res = (Response) client.sendRequest(new Request("GET_ROOM_BY_ID", roomId));
            Room room = (Room) res.getPayload();
            Response res1 = (Response) client.sendRequest(new Request("GET_ROOMUSER_BY_ID", String.valueOf(client.getPlayer().getId()) + " " + String.valueOf(roomId)));
            RoomUser roomuser = (RoomUser) res1.getPayload();
            if (room.isIs_start() == true) {
                new ExitDialog(this, rootPaneCheckingEnabled, client, this.roomId);
            } else {

                Response response = (Response) client.sendRequest(new Request("EXIT_ROOM", roomuser));
//                    JOptionPane.showMessageDialog(null, "Rời phòng thành công !");
                client.roomView = null;
                this.dispose();
                LobbyView lb;
                lb = new LobbyView(client, client.getPlayer());
                lb.setVisible(true);
                client.setLobbyView(lb);

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ExitButtonActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
        String text = fillArea.getText();
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng không để trống tin nhắn !");
        } else {
            String item = (String) userCombo.getSelectedItem();
            try {
                Response res = (Response) client.sendRequest(new Request("SEND_MESSAGE", text + "//" + item + "//" + String.valueOf(client.getPlayer().getId())));
                String message = (String) res.getPayload();
                if (message.equals("OK")) {
//                    JOptionPane.showMessageDialog(null, "Gửi thành công !");
                    fillArea.setText("");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_sendBtnActionPerformed

    private void chat4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chat4ActionPerformed

    private void invite4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invite4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invite4ActionPerformed

    private void chat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chat1ActionPerformed

    private void readyButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readyButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_readyButton1ActionPerformed

    private void chat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chat2ActionPerformed

    private void invite2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invite2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invite2ActionPerformed

    private void chat3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chat3ActionPerformed

    private void invite3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invite3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invite3ActionPerformed
    public void updateChat(String message) {
        chatArea.append(message);
        chatArea.append("\n");
    }

    private void showInviteDialog() throws InterruptedException {
        JDialog dialog = new JDialog(this, "Mời người chơi", true);

        dialog.setSize(450, 350);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        // Nút đóng dialog
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JButton closeButton = new JButton("X");
//        closeButton.addActionListener(e -> dialog.dispose());
//        headerPanel.add(closeButton, BorderLayout.EAST);
//
//        // Thêm panel header vào dialog
//        dialog.add(headerPanel, BorderLayout.NORTH);

        // Panel chứa danh sách người chơi
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new GridLayout(2, 2)); // Ví dụ: lưới 2x2



        // Lấy danh sách người chơi online từ server
        try {
            Response response = (Response) client.sendRequest(new Request("GET_USER_ONLINE", client.getPlayer().getId()));
            ArrayList<String> onlinePlayers = (ArrayList<String>) response.getPayload();
            for (String player : onlinePlayers) {
                JPanel playerPanel = new JPanel(new BorderLayout());
                JLabel playerNameLabel = new JLabel(player);
                playerPanel.add(playerNameLabel, BorderLayout.WEST);

                JButton inviteButton = new JButton("Mời");
                inviteButton.addActionListener(e -> {

                    try {
                        Response res = (Response) client.sendRequest(new Request("GET_USERNAME_BY_ID", player));
                        Player p = (Player) res.getPayload();
                        Response rp = (Response) client.sendRequest(new Request("SEND_INVITE", String.valueOf(p.getId()) + " " + String.valueOf(client.getPlayer().getId())));
                        String message = (String) rp.getPayload();
                        if (message.equals("ANOTHER_ROOM")) {
                            JOptionPane.showMessageDialog(null, "Người chơi này đã ở phòng khác , vui lòng thử lại sau !");
                        } else if (message.equals("DUPLICATE")) {
                            JOptionPane.showMessageDialog(null, "Người chơi đó hiện tại đang ở trong phòng bạn !");
                        } else if (message.equals("GAME_START")) {
                            JOptionPane.showMessageDialog(null, "Game đã bắt đầu , mời cái lồn !");
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                playerPanel.add(inviteButton, BorderLayout.EAST);
                playersPanel.add(playerPanel, BorderLayout.WEST);
            }

            JScrollPane scrollPane = new JScrollPane(playersPanel);
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setVisible(true);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void updatePlayerPosition() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        // Lấy phòng hiện tại từ listRoom dựa vào roomId
        Response res = (Response) client.sendRequest(new Request("GET_ALL_ROOMUSER", null));
        ArrayList<RoomUser> list = (ArrayList<RoomUser>) res.getPayload();
        Player pos[] = new Player[5];
        for (RoomUser roomuser : list) {
            if (roomuser.getId_room() == this.roomId) {
                int userId = roomuser.getId_user();
                Response res1 = (Response) client.sendRequest(new Request("GET_PLAYER_BY_ID", userId));
                Player player = (Player) res1.getPayload();
                pos[roomuser.getPosition()] = player;
            }
        }
        if (this.client.FlipCardPanel != null) {
            FlipCardPanel fc = (FlipCardPanel) client.FlipCardPanel;
            fc.displayCardPlayer();
        }
        userCombo.removeAllItems();
        userCombo.addItem("ALL");
        for (int i = 1; i < 5; i++) {
            if (pos[i] != null) {
                Response res1 = (Response) client.sendRequest(new Request("GET_ROOMUSER_BY_USER_ID", pos[i].getId()));
                RoomUser ru = (RoomUser) res1.getPayload();
                boolean is_ready = ru.isIs_ready();
                if (is_ready) {
                    readyButton[i - 1].setVisible(true);
                    if (pos[i].getId() != client.getPlayer().getId()) {
                        readyButton[i - 1].setEnabled(false);
                    }
                } else {
                    readyButton[i - 1].setVisible(false);
                }
                if (pos[i].getId() != client.getPlayer().getId()) {
                    userCombo.addItem(pos[i].getUsername());
                    chatButton[i - 1].setVisible(true);
                }
                imageUser[i - 1].setVisible(true);
                nameUser[i - 1].setVisible(true);
                nameUser[i - 1].setText(pos[i].getUsername());
                inviteButton[i - 1].setVisible(false);
            } else {
                chatButton[i - 1].setVisible(false);
                imageUser[i - 1].setVisible(false);
                nameUser[i - 1].setVisible(false);
                inviteButton[i - 1].setVisible(true);
                inviteButton[i - 1].addActionListener(e -> {
                    try {
                        showInviteDialog();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoomView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
//            playerPanels[i - 1].revalidate();
//            playerPanels[i - 1].repaint();
        }
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CenterPanel;
    private javax.swing.JButton ExitButton;
    private javax.swing.JPanel JUSER1;
    private javax.swing.JPanel JUSER2;
    private javax.swing.JPanel JUSER3;
    private javax.swing.JPanel JUSER4;
    private javax.swing.JPanel cardPanel1;
    private javax.swing.JPanel cardPanel2;
    private javax.swing.JPanel cardPanel3;
    private javax.swing.JPanel cardPanel4;
    private javax.swing.JLabel cardUser1;
    private javax.swing.JLabel cardUser10;
    private javax.swing.JLabel cardUser11;
    private javax.swing.JLabel cardUser12;
    private javax.swing.JLabel cardUser2;
    private javax.swing.JLabel cardUser3;
    private javax.swing.JLabel cardUser4;
    private javax.swing.JLabel cardUser5;
    private javax.swing.JLabel cardUser6;
    private javax.swing.JLabel cardUser7;
    private javax.swing.JLabel cardUser8;
    private javax.swing.JLabel cardUser9;
    private javax.swing.JButton chat1;
    private javax.swing.JButton chat2;
    private javax.swing.JButton chat3;
    private javax.swing.JButton chat4;
    private javax.swing.JTextArea chatArea;
    private javax.swing.JTextArea fillArea;
    private javax.swing.JButton invite1;
    private javax.swing.JButton invite2;
    private javax.swing.JButton invite3;
    private javax.swing.JButton invite4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton readyButton1;
    private javax.swing.JButton readyButton2;
    private javax.swing.JButton readyButton3;
    private javax.swing.JButton readyButton4;
    private javax.swing.JLabel roomIdLabel;
    private javax.swing.JButton sendBtn;
    private javax.swing.JComboBox<String> userCombo;
    private javax.swing.JLabel userImage1;
    private javax.swing.JLabel userImage2;
    private javax.swing.JLabel userImage3;
    private javax.swing.JLabel userImage4;
    private javax.swing.JLabel userLabel1;
    private javax.swing.JLabel userLabel2;
    private javax.swing.JLabel userLabel3;
    private javax.swing.JLabel userLabel4;
    // End of variables declaration//GEN-END:variables
}
