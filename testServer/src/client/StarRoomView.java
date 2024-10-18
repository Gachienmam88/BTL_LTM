/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import model.Match;
import model.Request;
import model.Response;
import model.RoomUser;
import java.sql.Timestamp;

/**
 *
 * @author chipc
 */
public class StarRoomView extends javax.swing.JPanel {

    Client client;

    public StarRoomView(Client client) {
        this.client = client;
        client.startPanel = this;
        initComponents();
        countDownLabel.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        countDownLabel = new javax.swing.JLabel();

        startButton.setText("Bắt đầu");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        countDownLabel.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(218, 218, 218)
                        .addComponent(startButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(countDownLabel)))
                .addContainerGap(398, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(countDownLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(startButton)
                .addGap(68, 68, 68))
        );
    }// </editor-fold>//GEN-END:initComponents
    public void startCountDown() {
        int[] timeRemaining = {2};
        countDownLabel.setVisible(true);
        if (timeRemaining[0] == 2) {
            countDownLabel.setText("Chuẩn bị trong : 3 giây(s)");
        }
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDownLabel.setText("Chuẩn bị trong : " + timeRemaining[0] + " giây(s)"); // Cập nhật label

                timeRemaining[0]--; // Giảm thời gian còn lại

                if (timeRemaining[0] < 0) {
                    ((Timer) e.getSource()).stop(); // Dừng timer khi hết thời gian
                    countDownLabel.setText("Bắt đầu!");
                    new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ((Timer) e.getSource()).stop(); // Dừng timer sau khi chờ 1 giây
                            // Thực hiện logic bắt đầu trò chơi
                        }
                    }).start();
                    client.roomView.playingGame();
                }
            }
        });

        // Bắt đầu đếm ngược
        timer.start();
    }
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        try {
            Response res = (Response) client.sendRequest(new Request("CLIENT_READY", client.getPlayer().getId()));
            String message = (String) res.getPayload();
            if (message.equals("OK")) {
                startButton.setVisible(false);
//                this.revalidate();        // Cập nhật layout
//                this.repaint();
                client.roomView.updatePlayerPosition();
            } else {
                JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra , vui lòng thử lại sau !");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StarRoomView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StarRoomView.class.getName()).log(Level.SEVERE, null, ex); }
         catch (SQLException ex) {
            Logger.getLogger(StarRoomView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(StarRoomView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startButtonActionPerformed
    public void displayStartButton() {
        startButton.setVisible(true);
        this.revalidate();        // Cập nhật layout
        this.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel countDownLabel;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
