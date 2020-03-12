package server.Handler;

import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RedirectHandler extends HTTPHandler{
    public RedirectHandler(Socket clientSocket, String requestHeader, File root, StatusCodes statusCodes, String newpage) throws FileNotFoundException {
        clientSocket = clientSocket;
        requestSHeader = requestHeader;
        requestedFile = root;
        requestedFile = getFile(rootDirectory.getAbsolutePath() + newpage);
        responseGenerator = new ResponseGenerator(statusCodes, true, newpage, 0);
        generateResponseHeader();
    }

    @Override
    public void handle() {
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

            outputStreamWriter.write(responseHeader, 0, responseHeader.length());
            outputStreamWriter.flush();
        }catch(IOException e){
            System.err.println("can't write to Stream");
        }
    }
}
