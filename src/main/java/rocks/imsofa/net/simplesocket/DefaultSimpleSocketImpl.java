/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rocks.imsofa.net.simplesocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
public class DefaultSimpleSocketImpl implements SimpleSocket{
    private ExecutorService executorService=null;
    private int port=-1;
    private ServerSocket serverSocket=null;
    private ServerThread serverThread=null;
    private List<MessageCallback> callbacks=new ArrayList<>();
    
    public DefaultSimpleSocketImpl(int port){
        this.port=port;
    }
    
    public DefaultSimpleSocketImpl(){
    }
    
    @Override
    public String sendForResponse(String ip, int port, String message) throws Exception {
        Socket socket=new Socket(ip, port);
        try(OutputStream output=socket.getOutputStream(); InputStream input=socket.getInputStream()){
            IOUtils.write(message, output, "utf-8");
            output.flush();
            String ret=IOUtils.toString(input, "utf-8");
            socket.close();
            return ret;
        }
    }

    @Override
    public void send(String ip, int port, String message) throws Exception {
        Socket socket=new Socket(ip, port);
        try(OutputStream output=socket.getOutputStream(); InputStream input=socket.getInputStream()){
            IOUtils.write(message, output, "utf-8");
            output.flush();
        }
    }

    @Override
    public void addMessageCallback(MessageCallback messageCallback) {
        this.callbacks.add(messageCallback);
    }

    @Override
    public void startReceivingMessage() {
        if(serverThread==null){
            executorService=Executors.newFixedThreadPool(5);
            try {
                serverSocket=new ServerSocket(port);
                serverThread=new ServerThread();
                serverThread.start();
            } catch (IOException ex) {
                Logger.getLogger(DefaultSimpleSocketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stopReceivingMessage() {
        if(serverThread!=null){
            try {
                executorService.shutdown();
                executorService=null;
                serverThread.shutdown();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(DefaultSimpleSocketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            serverThread=null;
            serverSocket=null;
        }
    }
    
    class ServerThread extends Thread{
        private boolean running=true;
        public ServerThread(){
            setDaemon(true);
        }
        public void run(){
            while(running){
                try {
                    final Socket socket=serverSocket.accept();
                    executorService.submit(new Runnable(){
                        @Override
                        public void run() {
                            try(InputStream input=socket.getInputStream()){
                                String message=IOUtils.toString(input, "utf-8");
                                for(MessageCallback callback : callbacks){
                                    callback.onMessage(socket.getInetAddress().getHostAddress(), socket.getPort(), message);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(DefaultSimpleSocketImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                } catch (IOException ex) {
                    Logger.getLogger(DefaultSimpleSocketImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public void shutdown(){
            running=false;
        }
    }
}
