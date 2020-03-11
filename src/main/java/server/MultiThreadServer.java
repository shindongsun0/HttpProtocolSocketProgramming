package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    ServerSocket serverSocket;
    private int port;

    public MultiThreadServer(int port){
        this.port = port;
    }
    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Starting the socket server at port: " + port);
            ExecutorService executorService = Executors.newCachedThreadPool();

            Socket clientSocket = null;
            while(!executorService.isShutdown()){
                executorService.execute(new ClientHandler(serverSocket.accept()));
            }
        }catch(IOException e){
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
        }
    }
}
