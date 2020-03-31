package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;
import sun.java2d.pipe.ValidatePipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Arrays;

@Slf4j
public class GETHandler extends HTTPHandler{
    public GETHandler(Socket socket, String requestHeader, File root) throws FileNotFoundException {
        rootDirectory = root;
        requestSHeader = requestHeader;
        clientSocket = socket;
        requestedFile = getFile(validatePath());
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

            writeMessageBody(requestedFile);
            writer.flush();
        } catch (IOException e) {
            log.error("can't write to stream : {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
