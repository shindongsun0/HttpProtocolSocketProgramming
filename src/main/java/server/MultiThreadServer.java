package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            File filePath = new File("index.html");

            Socket clientSocket = null;
            while(!executorService.isShutdown()){
                executorService.execute(new ClientHandler(serverSocket.accept(), filePath));
            }
        }catch(IOException e){
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
        }
    }
    public static void main(String[] args){
        MultiThreadServer server = new MultiThreadServer(10005);
        if(!validRootPath(args[0])) {
            System.err.println("Provided root directory does not exist or is not a directory");
            System.exit(1);
        }
        server.start();
    }

    private static boolean validRootPath(String path){
        Path pathRoute = Paths.get(path);
        return !Files.notExists(pathRoute) && Files.isDirectory(pathRoute);
    }
}
