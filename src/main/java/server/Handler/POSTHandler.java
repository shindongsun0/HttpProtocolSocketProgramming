package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class POSTHandler extends HTTPHandler{
    private File postContentFile;
    private File locationToUpload;

    public POSTHandler(Socket socket, String requestHeader, File root) throws FileNotFoundException {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        requestedFile = getFile(getPathFromHeader());
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);
        postContentFile = null;

        if(!checkIfFolderExists()){
            createDirectory();
        }
    }

    @Override
    public void handle() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            postContentFile = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);

            try {
                FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
                String[] getData = requestSHeader.split("\r\n");
                int readBytes = getData[getData.length - 1].length();
                writeBytesToFileStream(fileStream, getData[getData.length - 1], 0, readBytes);
            } catch (FileNotFoundException e) {
                log.error("There was a problem with creating the file. {}", e.toString());
            }

            responseGenerator = new ResponseGenerator(StatusCodes.CREATED, true, rootDirectory.getAbsolutePath() + "/" + requestedFile, postContentFile.length());
            generateResponseHeader();

            outputStreamWriter.write(responseHeader, 0, responseHeader.length());
            outputStreamWriter.flush();
        }catch(IOException e){
            log.error("can't write to stream : {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private void writeBytesToFileStream(FileOutputStream file, String bodyData, int index, int length){
        try {
            file.write(bodyData.getBytes(), index, length);
        } catch (IOException e) {
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
}
