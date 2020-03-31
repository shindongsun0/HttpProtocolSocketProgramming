package server.Handler;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ErrorHandler extends HTTPHandler{
    public ErrorHandler(Socket socket, String requestHeader, File root, StatusCodes statusCode) {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        responseGenerator = new ResponseGenerator(statusCode);
        generateResponseHeader();
    }

    @Override
    public void handle() {
        try{
            OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
            writer.write(responseHeader, 0, responseHeader.length());
            writer.flush();
        } catch (IOException e) {
            log.error("can't write to Stream {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
