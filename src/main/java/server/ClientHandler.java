package server;

import lombok.extern.slf4j.Slf4j;
import server.Handler.ErrorHandler;
import server.Handler.GETHandler;
import server.Handler.HTTPHandler;
import server.Handler.POSTHandler;
import server.Response.StatusCodes;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Arrays;

@Slf4j
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private File rootDirectory;
    private String requestHeader;
    private BufferedReader reader;

    public ClientHandler(Socket clientSocket, File root) {
        this.rootDirectory = root;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        printNewThread(clientSocket);
        this.requestHeader = readResponseHeader();
        printResponseHeader(requestHeader);
        mappingHandler(requestHeader);
    }

    private void printNewThread(Socket socket) {
        InetAddress inetAddress = socket.getInetAddress();
        System.out.println(inetAddress.getHostAddress() + " connected to Server");
        System.out.println(Thread.currentThread().getName() + " started!!");
    }

    private BufferedReader readSetUp() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            log.error("Could not send data on port {}", clientSocket.getPort());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return this.reader;
    }

    public String readResponseHeader() {
        this.reader = readSetUp();
        StringBuilder builder = new StringBuilder();
        String line = null;
        do {
            try {
                line = this.reader.readLine();
            } catch (IOException e) {
                log.error("can't read stream {}", e.toString());
                log.error(Arrays.toString(e.getStackTrace()));
            }
            builder.append(line).append("\r\n");
        } while (!"".equals(line));
        return builder.toString();
    }

    private void printResponseHeader(String requestHeader) {
        System.out.println(requestHeader);
    }

    public boolean isHTTPRequest(String requestHeader) {
        if (!includesHTTP(requestHeader)) {
            System.out.println("NOT a HTTP request");
            try {
                clientSocket.close();
                return false;
            } catch (IOException e) {
                log.error("Could not close Socket {}", clientSocket);
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
        return true;
    }

    public void mappingHandler(String requestHeader) {
        if (isHTTPRequest(requestHeader)) {
            String requestType = getHTTPMethod(requestHeader);
            HTTPHandler handler = null;
            try {
                if (requestType.contentEquals("GET")) {
                    handler = new GETHandler(clientSocket, requestHeader, rootDirectory);
                } else if (requestType.contentEquals("POST")) {
                    handler = new POSTHandler(clientSocket, requestHeader, rootDirectory);
                }
            } catch (FileNotFoundException e) {
                //404
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.NOT_FOUND);
            } catch (IllegalArgumentException | SecurityException e) {
                //500
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.SERVER_ERROR);
            } catch (IndexOutOfBoundsException e) {
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.BAD_REQUEST);
            }

            try {
                if (handler != null)
                    handler.handle();
            } catch (AccessControlException e) {
                // 403
                handler = new ErrorHandler(clientSocket, requestHeader, rootDirectory, StatusCodes.FORBIDDEN);
                handler.handle();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client Socket close!");
                } catch (IOException e) {
                    log.error("can't close Client Socket {}", e.toString());
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    private String getHTTPMethod(String requestHeader) {
        String[] splitHeader = requestHeader.split("\\s");
        if (splitHeader.length == 0)
            throw new IndexOutOfBoundsException();
        return splitHeader[0];
    }

    private boolean includesHTTP(String requestHeader) {
        String[] splitHeader = requestHeader.split("\\s");
        if (splitHeader.length < 3) {
            return false;
        }
        return splitHeader[2].contains("HTTP");
    }
}
