package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class POSTHandler extends HTTPHandler{
    private File postContentFile;
    private File locationToUpload;
    public POSTHandler(Socket socket, String requestHeader, File root) {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        requestedFile = null;
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/upload");
        postContentFile = null;

        if(!checkIfFolderExists()){
            createDirectory();
        }
        responseGenerator = new ResponseGenerator(StatusCodes.CONTINUE);
        generateResponseHeader();
    }

    @Override
    public void handle() {
        try {
            InputStream inputStream = clientSocket.getInputStream();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

            outputStreamWriter.write(responseHeader, 0, responseHeader.length());

            int getByte;
            String getData = "";
            boolean endOfStream = false;
            while (endOfStream) {
                getByte = inputStream.read();
                char c = (char) getByte;
                getData += c;

                if(getData.contains("\r\n\r\n"))
                    endOfStream = true;
            }

        }catch(IOException e){
            log.error("can't write to stream : {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private boolean checkIfFolderExists(){
        return locationToUpload.exists();
    }

    private void createDirectory(){
        if(!locationToUpload.mkdir()){
            throw new SecurityException();
        }
    }

    private void writeToStream(FileOutputStream fileOutputStream, byte[] buffer, int offset, int length){
        try{
            fileOutputStream.write(buffer, offset, length);
        }catch(FileNotFoundException e){
            log.error("Cannot find file.");
        } catch(IOException e){
            log.error("cannot write to fileStream");
        }
    }


}
