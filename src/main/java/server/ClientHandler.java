package server;

import server.Handler.*;
import server.Response.StatusCodes;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;


public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private File rootDirectory;
    private String requestHeader;

    public ClientHandler(Socket clientSocket, File root) {
        this.rootDirectory= root;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        printNewThread();
        mappingHandler();
    }

    private void printNewThread() {
        InetAddress inetAddress = clientSocket.getInetAddress();
        System.out.println(inetAddress.getHostAddress() + " connected to Server");
        System.out.println(Thread.currentThread().getName() + " started!!");
    }

    private BufferedReader readSetUp(){
        BufferedReader request = null;
        try {
            InputStream inputStream = clientSocket.getInputStream();
            request = new BufferedReader(new InputStreamReader(inputStream));
        }catch(IOException e){
            System.out.println("Could not send data on port " + clientSocket.getPort());
        }
        return request;
    }

    public String readResponseHeader() {
        BufferedReader request = readSetUp();
        StringBuilder builder = new StringBuilder();
        String line = null;
        do {
            try {
                line = request.readLine();
            } catch (IOException e) {
                System.out.println(e.toString());
                System.out.println(Arrays.asList(e.getStackTrace()));
            }
            builder.append(line).append("\r\n");
        } while (line.getBytes().length != 0);

        return builder.toString();
    }

    public boolean decideHTTPRequest(){
        requestHeader = readResponseHeader();
        System.out.println(requestHeader);

        if (!isHTTPRequest()) {
            System.out.println("NOT a HTTP request");
            try {
                clientSocket.close();
                return false;
            } catch(IOException e){
                System.out.println("Could not close Socket " + clientSocket);
            }
        }
        return true;
    }

    public void mappingHandler(){
        if(decideHTTPRequest()) {
            String requestType = getHTTPMethod();
            HTTPHandler handler = null;
            try {
                if (getPathFromHeader().contentEquals("/oldpage.html")) {
                    handler = new RedirectHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.FOUND, "newpage");
                } else if (requestType.contentEquals("GET")) {
                    handler = new GETHandler(clientSocket, requestHeader, rootDirectory);
                } else if (requestType.contentEquals("POST")) {
                    handler = new POSTHandler(clientSocket, requestHeader, rootDirectory);
                }
            } catch (FileNotFoundException e) {
                //404 FILE NOT FOUND
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, requestType);
            } catch (IllegalArgumentException | SecurityException e) {
                //500 INTERNAL SERVER ERROR
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.SERVER_ERROR);
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client Socket close!");
                } catch (IOException e) {
                    System.out.println("Cannot close client socket");
                }
            }
        }
    }

    private String getHTTPMethod() {
        String[] splitHeader = requestHeader.split("\\s");
        if(splitHeader.length == 0)
            throw new IndexOutOfBoundsException();
        return splitHeader[0];
    }

    private String getPathFromHeader(){
        String[] splitHeader = requestHeader.split("\\s");
        if(splitHeader.length < 1)
            throw new IndexOutOfBoundsException();
        return splitHeader[1];
    }

    private boolean isHTTPRequest() {
        String[] splitHeader = requestHeader.split("\\s");
        if(splitHeader.length < 3){
            return false;
        }
        return splitHeader[2].contains("HTTP");
    }
}
