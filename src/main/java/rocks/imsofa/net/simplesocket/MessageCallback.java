/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rocks.imsofa.net.simplesocket;

/**
 *
 * @author lendle
 */
public interface MessageCallback {
    public void onMessage(String sourceIp, int sourcePort, String message);
}
