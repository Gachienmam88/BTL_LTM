/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Dialog.java to edit this template
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.Card;
import model.MatchUser;
import model.Player;
import model.Request;
import model.Response;

/**
 *
 * @author chipc
 */
public class Result extends java.awt.Dialog {

    /**
     * Creates new form Result
     */
    private Client client;
    private int match_id;
    private JLabel[] userLabel = new JLabel[4];
    private JLabel[] imageLabel = new JLabel[12];
    private JLabel[] totalLabel = new JLabel[4];
    private JPanel[] userPanel = new JPanel[4];
    private Timer countdownTimer;
    private int timeRemaining = 5;
    private int room_id;

    public Result(java.awt.Frame parent, boolean modal, Client client, int match_id) throws ClassNotFoundException, IOException, InterruptedException {
        super(parent, modal);
        initComponents();
        this.client = client;
        this.match_id = match_id;
        if (client.roomView != null) {
            this.room_id = client.roomView.getRoomId();
        }
        userLabel[0] = user1;
        userLabel[1] = user2;
        userLabel[2] = user3;
        userLabel[3] = user4;
        imageLabel[0] = card1;
        imageLabel[1] = card2;
        imageLabel[2] = card3;
        imageLabel[3] = card4;
        imageLabel[4] = card5;
        imageLabel[5] = card6;
        imageLabel[6] = card7;
        imageLabel[7] = card8;
        imageLabel[8] = card9;
        imageLabel[9] = card10;
        imageLabel[10] = card11;
        imageLabel[11] = card12;
        totalLabel[0] = total1;
        totalLabel[1] = total2;
        totalLabel[2] = total3;
        totalLabel[3] = total4;
        userPanel[0] = panelUser1;
        userPanel[1] = panelUser2;
        userPanel[2] = panelUser3;
        userPanel[3] = panelUser4;
        displayDialog();
        startCountdown();

        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void hiddenPanel() {
        for (int i = 0; i < 4; i++) {
            userPanel[i].setVisible(false);
        }
    }

    private void startCountdown() {

        countdownTimer = new Timer(1000, new ActionListener() {  // 1000ms = 1 giây
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;  // Giảm thời gian còn lại mỗi giây

                if (timeRemaining <= 0) {
                    countdownTimer.stop();  // Dừng bộ đếm khi hết thời gian

                    if (client.FlipCardPanel != null) {

                        setVisible(false);
                        client.roomView.addStartPanel();
                        try {
                            Response res = (Response) client.sendRequest(new Request("CANCEL_ROOM_START", String.valueOf(room_id) + " " + String.valueOf(client.getPlayer().getId())));
                            String message = (String) res.getPayload();
                            if (message.equals("OK")) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        Timer delayTimer = new Timer(500, new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                try {
                                                    client.FlipCardPanel = null;
                                                    client.roomView.updatePlayerPosition();
                                                    client.roomView.hiddenCardLabel();
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(FlipCardPanel.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (InterruptedException ex) {
                                                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        });
                                        delayTimer.setRepeats(false);  // Chỉ chạy một lần
                                        delayTimer.start();  // Bắt đầu Timer cho việc đóng dialog
                                    }
                                });

                            } else {
                                JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra, vui lòng thử lại sau !");
                            }
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        dispose();
                    }
                }
            }
        });
        countdownTimer.start();  // Bắt đầu đếm ngược
    }

    public void displayCard(int index, String image) {
        imageLabel[index].setText("");
        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        imageLabel[index].setIcon(icon);
    }

    public void displayDialog() throws ClassNotFoundException, IOException, InterruptedException {
        Response res = (Response) client.sendRequest(new Request("GET_DETAIL_MATCH", match_id));
        TreeMap<Integer, ArrayList<MatchUser>> list = (TreeMap<Integer, ArrayList<MatchUser>>) res.getPayload(); //comment 
        List<Map.Entry<Integer, ArrayList<MatchUser>>> sortedList = new ArrayList<>(list.entrySet());
//        for (Map.Entry<Integer, ArrayList<MatchUser>> mp : sortedList) {
//            System.out.println(mp.getKey());
//        }
        sortedList.sort((entry1, entry2) -> {
            ArrayList<MatchUser> player1Cards = entry1.getValue();
            ArrayList<MatchUser> player2Cards = entry2.getValue();
            List<String> sortedPlayer1Cards = new ArrayList<>();
            List<String> sortedPlayer2Cards = new ArrayList<>();
            int totalValue1 = 0;
            int totalValue2 = 0;
            for (MatchUser mu : player1Cards) {
                int idCard = mu.getCard_id();
                try {
                    Response resCard = (Response) client.sendRequest(new Request("GET_CARD_BY_ID", idCard));
                    Card card = (Card) resCard.getPayload();
                    String value = card.getValue();
                    char number = value.charAt(0);
                    int valueCard = Character.getNumericValue(number);
                    sortedPlayer1Cards.add(value);
                    totalValue1 += valueCard;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            for (MatchUser mu : player2Cards) {
                int idCard = mu.getCard_id();
                try {
                    Response resCard = (Response) client.sendRequest(new Request("GET_CARD_BY_ID", idCard));
                    Card card = (Card) resCard.getPayload();
                    String value = card.getValue();
                    char number = value.charAt(0);
                    int valueCard = Character.getNumericValue(number);
                    sortedPlayer2Cards.add(value);
                    totalValue2 += valueCard;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            // Tính tổng điểm cho player1 và player2

            if (totalValue1 != totalValue2) {
                return Integer.compare(totalValue2, totalValue1); // Sắp xếp tổng điểm từ cao đến thấp
            }

            // Nếu tổng điểm bằng nhau, so sánh 2 lá bài lớn nhất
            sortedPlayer1Cards.sort((card1, card2) -> {
                char a1 = card1.charAt(0);
                char b1 = card1.charAt(1);
                char a2 = card2.charAt(0);
                char b2 = card2.charAt(1);
                int valueA = Character.getNumericValue(a1);
                int valueB = Character.getNumericValue(b1);
                return Integer.compare(valueB, valueA);
            });
            sortedPlayer2Cards.sort((card1, card2) -> {
                char a1 = card1.charAt(0);
                char b1 = card1.charAt(1);
                char a2 = card2.charAt(0);
                char b2 = card2.charAt(1);
                int valueA = Character.getNumericValue(a1);
                int valueB = Character.getNumericValue(b1);
                return Integer.compare(valueB, valueA);
            });

            if (!sortedPlayer1Cards.get(0).equals(sortedPlayer2Cards.get(0))) {
                char a1 = sortedPlayer1Cards.get(0).charAt(0);
                int valueA = Character.getNumericValue(a1);
                char b1 = sortedPlayer1Cards.get(0).charAt(1);
                char a2 = sortedPlayer2Cards.get(0).charAt(0);
                int valueB = Character.getNumericValue(a2);
                char b2 = sortedPlayer2Cards.get(0).charAt(1);
                if (valueA != valueB) {
                    return Integer.compare(valueB, valueA);
                } else {
                    return compareCardSuit(b2, b1);
                }
            }

            // So sánh 2 lá bài lớn nhất
            return 0;
        });
        for (int i = 1; i <= 4; i++) {
            if (i <= sortedList.size()) {
                userPanel[i - 1].setVisible(true);
                Response resPlayer = (Response) client.sendRequest(new Request("GET_PLAYER_BY_ID", sortedList.get(i - 1).getKey()));
                Player player = (Player) resPlayer.getPayload();
                userLabel[i - 1].setText(player.getUsername());
                this.revalidate();        // Cập nhật layout
                this.repaint();
                displayImageAndTotal(sortedList, sortedList.get(i - 1).getKey(), i - 1);
                Response resDash = (Response) client.sendRequest(new Request("ADD_RATING_USER", String.valueOf(player.getId()) + " " + String.valueOf(match_id) + " " + String.valueOf(i)));
                String message = (String) resDash.getPayload();
                if (!message.equals("OK")) {
                    JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra , vui lòng thử lại sau !");
                }
            } else {
                userPanel[i - 1].setVisible(false);
            }
        }

    }

    ;
    private void displayImageAndTotal(List<Map.Entry<Integer, ArrayList<MatchUser>>> sortedList, int playerId, int index) throws ClassNotFoundException, IOException, InterruptedException {
        int count = 0;
        int total = 0;
        String name;
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<MatchUser>> mp : sortedList) {
            if (mp.getKey() == playerId) {
                for (MatchUser mu : mp.getValue()) {
                    int idCard = mu.getCard_id();
                    Response resCard = (Response) client.sendRequest(new Request("GET_CARD_BY_ID", idCard));
                    Card card = (Card) resCard.getPayload();
                    String value = card.getValue();
                    String image = card.getImage();
                    int word = Character.getNumericValue(value.charAt(0));
                    total += word;
                    list.add(image);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            displayCard(index * 3 + i, list.get(i));
        }

        totalLabel[index].setText("Tổng điểm : " + total);
    }

    private int compareCardSuit(char suit1, char suit2) {
        String order = "SCDH";
        return Integer.compare(order.indexOf(suit1), order.indexOf(suit2));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelUser1 = new javax.swing.JPanel();
        user1 = new javax.swing.JLabel();
        card1 = new javax.swing.JLabel();
        card2 = new javax.swing.JLabel();
        card3 = new javax.swing.JLabel();
        total1 = new javax.swing.JLabel();
        panelUser2 = new javax.swing.JPanel();
        user2 = new javax.swing.JLabel();
        card4 = new javax.swing.JLabel();
        card5 = new javax.swing.JLabel();
        card6 = new javax.swing.JLabel();
        total2 = new javax.swing.JLabel();
        panelUser3 = new javax.swing.JPanel();
        user3 = new javax.swing.JLabel();
        card7 = new javax.swing.JLabel();
        card8 = new javax.swing.JLabel();
        card9 = new javax.swing.JLabel();
        total3 = new javax.swing.JLabel();
        panelUser4 = new javax.swing.JPanel();
        user4 = new javax.swing.JLabel();
        card10 = new javax.swing.JLabel();
        card11 = new javax.swing.JLabel();
        card12 = new javax.swing.JLabel();
        total4 = new javax.swing.JLabel();

        jLabel10.setText("jLabel10");

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Bảng xếp hạng");

        user1.setText("jLabel2");

        card1.setText("jLabel3");

        card2.setText("jLabel4");

        card3.setText("jLabel5");

        total1.setText("jLabel6");

        javax.swing.GroupLayout panelUser1Layout = new javax.swing.GroupLayout(panelUser1);
        panelUser1.setLayout(panelUser1Layout);
        panelUser1Layout.setHorizontalGroup(
            panelUser1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(user1)
                .addGap(105, 105, 105)
                .addComponent(card1)
                .addGap(18, 18, 18)
                .addComponent(card2)
                .addGap(18, 18, 18)
                .addComponent(card3)
                .addGap(62, 62, 62)
                .addComponent(total1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUser1Layout.setVerticalGroup(
            panelUser1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelUser1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(user1)
                    .addComponent(card1)
                    .addComponent(card2)
                    .addComponent(card3)
                    .addComponent(total1))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        user2.setText("jLabel7");

        card4.setText("jLabel8");

        card5.setText("jLabel9");

        card6.setText("jLabel11");

        total2.setText("jLabel12");

        javax.swing.GroupLayout panelUser2Layout = new javax.swing.GroupLayout(panelUser2);
        panelUser2.setLayout(panelUser2Layout);
        panelUser2Layout.setHorizontalGroup(
            panelUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(user2)
                .addGap(104, 104, 104)
                .addComponent(card4)
                .addGap(18, 18, 18)
                .addComponent(card5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card6)
                .addGap(57, 57, 57)
                .addComponent(total2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUser2Layout.setVerticalGroup(
            panelUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(user2)
                    .addComponent(card4)
                    .addComponent(card5)
                    .addComponent(card6)
                    .addComponent(total2))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        user3.setText("jLabel13");

        card7.setText("jLabel14");

        card8.setText("jLabel15");

        card9.setText("jLabel16");

        total3.setText("jLabel17");

        javax.swing.GroupLayout panelUser3Layout = new javax.swing.GroupLayout(panelUser3);
        panelUser3.setLayout(panelUser3Layout);
        panelUser3Layout.setHorizontalGroup(
            panelUser3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(user3)
                .addGap(94, 94, 94)
                .addComponent(card7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card9)
                .addGap(58, 58, 58)
                .addComponent(total3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUser3Layout.setVerticalGroup(
            panelUser3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelUser3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(user3)
                    .addComponent(card7)
                    .addComponent(card8)
                    .addComponent(card9)
                    .addComponent(total3))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        user4.setText("jLabel18");

        card10.setText("jLabel19");

        card11.setText("jLabel20");

        card12.setText("jLabel21");

        total4.setText("jLabel22");

        javax.swing.GroupLayout panelUser4Layout = new javax.swing.GroupLayout(panelUser4);
        panelUser4.setLayout(panelUser4Layout);
        panelUser4Layout.setHorizontalGroup(
            panelUser4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(user4)
                .addGap(95, 95, 95)
                .addComponent(card10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card11)
                .addGap(12, 12, 12)
                .addComponent(card12)
                .addGap(59, 59, 59)
                .addComponent(total4)
                .addContainerGap(181, Short.MAX_VALUE))
        );
        panelUser4Layout.setVerticalGroup(
            panelUser4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUser4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(panelUser4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(user4)
                    .addComponent(card10)
                    .addComponent(card11)
                    .addComponent(card12)
                    .addComponent(total4))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelUser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelUser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(panelUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelUser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelUser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        if (client.FlipCardPanel != null) {
            setVisible(false);
            client.roomView.addStartPanel();
            try {
                Response res = (Response) client.sendRequest(new Request("CANCEL_ROOM_START", String.valueOf(room_id) + " " + String.valueOf(client.getPlayer().getId())));
                String message = (String) res.getPayload();
                if (message.equals("OK")) {
                    client.FlipCardPanel = null;
                    client.roomView.updatePlayerPosition();
                    client.roomView.hiddenCardLabel();
                } else {
                    JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra, vui lòng thử lại sau !");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            dispose();
        }

    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel card1;
    private javax.swing.JLabel card10;
    private javax.swing.JLabel card11;
    private javax.swing.JLabel card12;
    private javax.swing.JLabel card2;
    private javax.swing.JLabel card3;
    private javax.swing.JLabel card4;
    private javax.swing.JLabel card5;
    private javax.swing.JLabel card6;
    private javax.swing.JLabel card7;
    private javax.swing.JLabel card8;
    private javax.swing.JLabel card9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel panelUser1;
    private javax.swing.JPanel panelUser2;
    private javax.swing.JPanel panelUser3;
    private javax.swing.JPanel panelUser4;
    private javax.swing.JLabel total1;
    private javax.swing.JLabel total2;
    private javax.swing.JLabel total3;
    private javax.swing.JLabel total4;
    private javax.swing.JLabel user1;
    private javax.swing.JLabel user2;
    private javax.swing.JLabel user3;
    private javax.swing.JLabel user4;
    // End of variables declaration//GEN-END:variables
}
