/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rocks.imsofa.net.simplesocket;

/**
 *
 * @author lendle
 */
public class Test1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        SimpleSocket ss=new DefaultSimpleSocketImpl(8081);
        SimpleSocket ss1=new DefaultSimpleSocketImpl();
        
        ss.addMessageCallback(new MessageCallback(){
            @Override
            public void onMessage(String sourceIp, int sourcePort, String message) {
                System.out.println(sourceIp+":"+sourcePort+":"+message);
            }
        });
        
        ss.startReceivingMessage();
        ss1.send("localhost", 8081, "This is a test.");
        Thread.sleep(30000);
        ss.stopReceivingMessage();
    }
    
}
