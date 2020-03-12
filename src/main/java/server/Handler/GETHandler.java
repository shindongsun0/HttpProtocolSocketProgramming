package server.Handler;

import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.AccessControlException;

public class GETHandler extends HTTPHandler{
    public GETHandler(Socket socket, String requestHeader, File root) throws FileNotFoundException {
        rootDirectory = root;
        requestSHeader = requestHeader;
        clientSocket = socket;
        requestedFile = getFile(rootDirectory.getAbsolutePath()+getPathFromHeader());
        responseGenerator = new ResponseGenerator(StatusCodes.OK, getFileType(requestedFile.getAbsolutePath()), requestedFile.length());
        generateResponseHeader();
    }

    @Override
    public void handle() {
        if(!requestedFile.canRead()){
            throw new AccessControlException("cannot read file");
        }
        try{
            OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
            writer.write(responseHeader, 0, responseHeader.length());
            writer.flush();

            writeFileToStream(requestedFile);
            writer.flush();
        } catch (IOException e) {
            System.err.println("can't write to stream");
        }
    }
}
