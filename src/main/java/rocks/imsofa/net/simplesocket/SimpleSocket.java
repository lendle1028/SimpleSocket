/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rocks.imsofa.net.simplesocket;

/**
 *
 * @author lendle
 */
public interface SimpleSocket {
    public String sendForResponse(String ip, int port, String message) throws Exception;
    public void send(String ip, int port, String message) throws Exception;
    public void addMessageCallback(MessageCallback messageCallback);
    public void startReceivingMessage();
    public void stopReceivingMessage();
}
