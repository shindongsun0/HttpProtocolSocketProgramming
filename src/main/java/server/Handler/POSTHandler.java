package server.Handler;

import java.io.File;
import java.net.Socket;

public class POSTHandler extends HTTPHandler{
    public POSTHandler(Socket clientSocket, String requestHeader, File root) {
        clientSocket = clientSocket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        requestedFile = null;
    }

    @Override
    public void handle() {

    }
}
