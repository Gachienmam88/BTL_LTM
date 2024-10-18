/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author chipc
 */
public class AlertInformation {
    private String message;

    public AlertInformation(String message) {
        this.message = message;
    }
    public void act(){
        System.out.println(this.message);
    }
}
