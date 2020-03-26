package server.Handler;

import org.joda.time.format.DateTimeFormat;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Date;

public class ErrorHandler extends HTTPHandler{
    public ErrorHandler(Socket socket, String requestHeader, File root, String clientRequestType) {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        requestType = clientRequestType;
        responseGenerator = new ResponseGenerator(StatusCodes.NOT_FOUND, requestType);
        generateResponseHeader();
        handle();
    }

    public ErrorHandler(Socket clientSocket, String requestHeader, File rootDirectory, StatusCodes serverError) {
        super();
    }

    @Override
    public void handle() {
        try{
            OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
            writer.write(responseHeader, 0, responseHeader.length());
            writer.flush();
        } catch (IOException e) {
            System.err.println("can't write to stream");
        }
    }
}
