/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import javax.swing.SwingUtilities;

/**
 *
 * @author chipc
 */
public class main {

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient("localhost", 5000);
        LoginView loginview = new LoginView(client);
        loginview.display();
    }
}
