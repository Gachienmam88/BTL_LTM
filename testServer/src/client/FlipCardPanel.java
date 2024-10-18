/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.MatchUser;
import model.Request;
import model.Response;
import model.Card;

/**
 *
 * @author chipc
 */
public class FlipCardPanel extends javax.swing.JPanel {

    /**
     * Creates new form FlipCardPanel
     */
    private String[] pathCard = new String[36];
    private ArrayList<String> setPathCard = new ArrayList<>();
    boolean mapping[] = new boolean[36];
    private JButton[] cardButton = new JButton[36];
    private int match_id;
    private Client client;
    private Timer countdownTimer;
    private int timeRemaining = 30;
    private int countCard = 0;
    private int roomId;

    public FlipCardPanel(Client client, int roomId) {
        this.roomId = roomId;
        this.client = client;
        client.FlipCardPanel = this;
        initComponents();
        setPathCard.add("/asset/image/2C.png");
        setPathCard.add("/asset/image/2D.png");
        setPathCard.add("/asset/image/2H.png");
        setPathCard.add("/asset/image/2S.png");
        setPathCard.add("/asset/image/3C.png");
        setPathCard.add("/asset/image/3D.png");
        setPathCard.add("/asset/image/3H.png");
        setPathCard.add("/asset/image/3S.png");
        setPathCard.add("/asset/image/4C.png");
        setPathCard.add("/asset/image/4D.png");
        setPathCard.add("/asset/image/4H.png");
        setPathCard.add("/asset/image/4S.png");
        setPathCard.add("/asset/image/5C.png");
        setPathCard.add("/asset/image/5D.png");
        setPathCard.add("/asset/image/5H.png");
        setPathCard.add("/asset/image/5S.png");
        setPathCard.add("/asset/image/6C.png");
        setPathCard.add("/asset/image/6D.png");
        setPathCard.add("/asset/image/6H.png");
        setPathCard.add("/asset/image/6S.png");
        setPathCard.add("/asset/image/7C.png");
        setPathCard.add("/asset/image/7D.png");
        setPathCard.add("/asset/image/7H.png");
        setPathCard.add("/asset/image/7S.png");
        setPathCard.add("/asset/image/8C.png");
        setPathCard.add("/asset/image/8D.png");
        setPathCard.add("/asset/image/8H.png");
        setPathCard.add("/asset/image/8S.png");
        setPathCard.add("/asset/image/9C.png");
        setPathCard.add("/asset/image/9D.png");
        setPathCard.add("/asset/image/9H.png");
        setPathCard.add("/asset/image/9S.png");
        setPathCard.add("/asset/image/1C.png");
        setPathCard.add("/asset/image/1D.png");
        setPathCard.add("/asset/image/1H.png");
        setPathCard.add("/asset/image/1S.png");
        cardButton[0] = card1;
        cardButton[1] = card2;
        cardButton[2] = card3;
        cardButton[3] = card4;
        cardButton[4] = card5;
        cardButton[5] = card6;
        cardButton[6] = card7;
        cardButton[7] = card8;
        cardButton[8] = card9;
        cardButton[9] = card10;
        cardButton[10] = card11;
        cardButton[11] = card12;
        cardButton[12] = card13;
        cardButton[13] = card14;
        cardButton[14] = card15;
        cardButton[15] = card16;
        cardButton[16] = card17;
        cardButton[17] = card18;
        cardButton[18] = card19;
        cardButton[19] = card20;
        cardButton[20] = card21;
        cardButton[21] = card22;
        cardButton[22] = card23;
        cardButton[23] = card24;
        cardButton[24] = card25;
        cardButton[25] = card26;
        cardButton[26] = card27;
        cardButton[27] = card28;
        cardButton[28] = card29;
        cardButton[29] = card30;
        cardButton[30] = card31;
        cardButton[31] = card32;
        cardButton[32] = card33;
        cardButton[33] = card34;
        cardButton[34] = card35;
        cardButton[35] = card36;
        for (int i = 0; i < 36; i++) {
            mapping[i] = true;
            int index = i;  // Lưu giữ giá trị của i, biến này sẽ là "effectively final"
            cardButton[index].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    countCard++;// Sử dụng chỉ số cố định
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(setPathCard.size() - 1);
                    String randomElement = setPathCard.get(randomIndex);
                    try {
                        Response res = (Response) client.sendRequest(new Request("FLIP_CARD", String.valueOf(match_id) + " " + String.valueOf(client.getPlayer().getId()) + " " + randomElement + " " + String.valueOf(index)));
                        String message = (String) res.getPayload();
                        if (message.equals("OK")) {
                            client.roomView.updatePlayerPosition();
                        } else {
                            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra, vui lòng thử lại sau !");
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        startCountdown();
        this.setVisible(true);
    }

    private void startCountdown() {
        countdownTimer = new Timer(1000, new ActionListener() {  // 1000ms = 1 giây
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;  // Giảm thời gian còn lại mỗi giây
                timeLabel.setText(String.valueOf(timeRemaining));
                if (timeRemaining < 10) {
                    timeLabel.setForeground(Color.red);
                }
                if (timeRemaining <= 0) {
                    countdownTimer.stop();  // Dừng bộ đếm khi hết thời gian
                    timeLabel.setText("Hết giờ!");
                    while (countCard < 3) {
                        getRandomCard();
                    }
                    timeLabel.setText("Đang tính toán kết quả");
                    // Hiển thị dialog và sau đó khởi động timer để đóng sau 2 giây
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            Timer delayTimer = new Timer(2000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Result result = new Result(client.roomView, true, client, match_id);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            delayTimer.setRepeats(false);  // Chỉ chạy một lần
                            delayTimer.start();  // Bắt đầu Timer cho việc đóng dialog
                        }
                    });
                }
            }
        });
        countdownTimer.start();  // Bắt đầu đếm ngược
    }

    public void setHiddenCard(int index, String path) {
        mapping[index] = false;
        setPathCard.remove(path);
        cardButton[index].setEnabled(false);
        cardButton[index].setOpaque(false);
        cardButton[index].setContentAreaFilled(false);
        cardButton[index].setBorderPainted(false);
        cardButton[index].setFocusPainted(false);
    }

    public void setMatchId(int match_id) {
        this.match_id = match_id;
    }

    public void getRandomCard() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            if (mapping[i] != false) {
                list.add(i);
            }
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(list.size() - 1);
        int randomE = list.get(randomIndex);
        cardButton[randomE].doClick();
    }

    public void displayCardPlayer() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
//        displayRealTime(cardButton, setPathCard);
        Response res = (Response) client.sendRequest(new Request("GET_MATCHUSER_BY_MATCH_ID", match_id));
        ArrayList<MatchUser> list = (ArrayList<MatchUser>) res.getPayload();
        for (MatchUser mu : list) {
            Response res1 = (Response) client.sendRequest(new Request("GET_CARD_BY_ID", mu.getCard_id()));
            Card card = (Card) res1.getPayload();
            client.roomView.displayCardLabel(mu.getPos(), new ImageIcon(getClass().getResource(card.getImage())));
        }
    }


    public void disableAllCards() {
        for (int i = 0; i < 36; i++) {
            cardButton[i].setEnabled(false);
        }
    }

    public int getMatch() {
        return this.match_id;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        card2 = new javax.swing.JButton();
        card3 = new javax.swing.JButton();
        card4 = new javax.swing.JButton();
        card5 = new javax.swing.JButton();
        card1 = new javax.swing.JButton();
        card6 = new javax.swing.JButton();
        card14 = new javax.swing.JButton();
        card15 = new javax.swing.JButton();
        card16 = new javax.swing.JButton();
        card17 = new javax.swing.JButton();
        card13 = new javax.swing.JButton();
        card18 = new javax.swing.JButton();
        card26 = new javax.swing.JButton();
        card27 = new javax.swing.JButton();
        card28 = new javax.swing.JButton();
        card29 = new javax.swing.JButton();
        card25 = new javax.swing.JButton();
        card30 = new javax.swing.JButton();
        card8 = new javax.swing.JButton();
        card9 = new javax.swing.JButton();
        card10 = new javax.swing.JButton();
        card11 = new javax.swing.JButton();
        card7 = new javax.swing.JButton();
        card12 = new javax.swing.JButton();
        card20 = new javax.swing.JButton();
        card21 = new javax.swing.JButton();
        card22 = new javax.swing.JButton();
        card23 = new javax.swing.JButton();
        card19 = new javax.swing.JButton();
        card24 = new javax.swing.JButton();
        card32 = new javax.swing.JButton();
        card33 = new javax.swing.JButton();
        card34 = new javax.swing.JButton();
        card35 = new javax.swing.JButton();
        card31 = new javax.swing.JButton();
        card36 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        card2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card2, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 49, -1, -1));

        card3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card3, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 49, -1, -1));

        card4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card4, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 49, -1, -1));

        card5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card5ActionPerformed(evt);
            }
        });
        add(card5, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 49, -1, -1));

        card1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 49, -1, -1));

        card6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card6, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 49, -1, -1));

        card14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card14, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 122, -1, -1));

        card15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card15, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 122, -1, -1));

        card16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card16, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 122, -1, -1));

        card17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card17ActionPerformed(evt);
            }
        });
        add(card17, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 122, -1, -1));

        card13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card13, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 122, -1, -1));

        card18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card18, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 122, -1, -1));

        card26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card26, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 189, -1, -1));

        card27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card27, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 189, -1, -1));

        card28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card28, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 189, -1, -1));

        card29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card29ActionPerformed(evt);
            }
        });
        add(card29, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 189, -1, -1));

        card25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card25, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 189, -1, -1));

        card30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card30, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 189, -1, -1));

        card8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card8, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 49, -1, -1));

        card9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card9, new org.netbeans.lib.awtextra.AbsoluteConstraints(418, 49, -1, -1));

        card10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card10, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 49, -1, -1));

        card11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card11ActionPerformed(evt);
            }
        });
        add(card11, new org.netbeans.lib.awtextra.AbsoluteConstraints(518, 49, -1, -1));

        card7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card7, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 49, -1, -1));

        card12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card12, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 49, -1, -1));

        card20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card20, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 122, -1, -1));

        card21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card21, new org.netbeans.lib.awtextra.AbsoluteConstraints(418, 122, -1, -1));

        card22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card22, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 122, -1, -1));

        card23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card23ActionPerformed(evt);
            }
        });
        add(card23, new org.netbeans.lib.awtextra.AbsoluteConstraints(518, 122, -1, -1));

        card19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card19, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 122, -1, -1));

        card24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card24, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 122, -1, -1));

        card32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card32, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 189, -1, -1));

        card33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card33.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card33, new org.netbeans.lib.awtextra.AbsoluteConstraints(418, 189, -1, -1));

        card34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card34.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card34, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 189, -1, -1));

        card35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card35.setMargin(new java.awt.Insets(0, 0, 0, 0));
        card35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card35ActionPerformed(evt);
            }
        });
        add(card35, new org.netbeans.lib.awtextra.AbsoluteConstraints(518, 189, -1, -1));

        card31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card31, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 189, -1, -1));

        card36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/image/card_back.png"))); // NOI18N
        card36.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(card36, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 189, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Thời gian còn lại : ");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 10, -1, -1));

        timeLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        timeLabel.setText("30");
        add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 6, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void card5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card5ActionPerformed

    private void card17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card17ActionPerformed

    private void card29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card29ActionPerformed

    private void card11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card11ActionPerformed

    private void card23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card23ActionPerformed

    private void card35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card35ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton card1;
    private javax.swing.JButton card10;
    private javax.swing.JButton card11;
    private javax.swing.JButton card12;
    private javax.swing.JButton card13;
    private javax.swing.JButton card14;
    private javax.swing.JButton card15;
    private javax.swing.JButton card16;
    private javax.swing.JButton card17;
    private javax.swing.JButton card18;
    private javax.swing.JButton card19;
    private javax.swing.JButton card2;
    private javax.swing.JButton card20;
    private javax.swing.JButton card21;
    private javax.swing.JButton card22;
    private javax.swing.JButton card23;
    private javax.swing.JButton card24;
    private javax.swing.JButton card25;
    private javax.swing.JButton card26;
    private javax.swing.JButton card27;
    private javax.swing.JButton card28;
    private javax.swing.JButton card29;
    private javax.swing.JButton card3;
    private javax.swing.JButton card30;
    private javax.swing.JButton card31;
    private javax.swing.JButton card32;
    private javax.swing.JButton card33;
    private javax.swing.JButton card34;
    private javax.swing.JButton card35;
    private javax.swing.JButton card36;
    private javax.swing.JButton card4;
    private javax.swing.JButton card5;
    private javax.swing.JButton card6;
    private javax.swing.JButton card7;
    private javax.swing.JButton card8;
    private javax.swing.JButton card9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
