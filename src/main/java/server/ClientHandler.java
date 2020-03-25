package server;

import server.Handler.*;
import server.Response.StatusCodes;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


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
        readResponse();
    }

    private void printNewThread() {
        InetAddress inetAddress = clientSocket.getInetAddress();
        System.out.println(inetAddress.getHostAddress() + " connected to Server");
        System.out.println(Thread.currentThread().getName() + " started!!");
    }

    private void readResponse(){
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader request = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            do {
                line = request.readLine();
                builder.append(line).append("\r\n");
            } while (line.getBytes().length != 0);

            requestHeader = builder.toString();
            System.out.println(requestHeader);
            if (!isHTTPRequest()) {
                System.out.println("NOT a HTTP request");
                clientSocket.close();
                return;
            }

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
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory);
            } catch (IllegalArgumentException | SecurityException e) {
                //500 INTERNAL SERVER ERROR
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.SERVER_ERROR);
            }
        } catch(IOException e){
            System.out.println("Could not send data on port " + clientSocket.getPort());
        }finally {
            try {
                clientSocket.close();
                System.out.println("Client Socket close!");
            }catch(IOException e){
                System.out.println("Cannot close client socket");
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
        if(!splitHeader[2].contains("HTTP"))
            return false;
        return true;
    }
}
