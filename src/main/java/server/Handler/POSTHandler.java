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
        responseGenerator = new ResponseGenerator(StatusCodes.CONTINUE);
        generateResponseHeader();
        handle();
    }

    @Override
    public void handle() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

            outputStreamWriter.write(responseHeader, 0, responseHeader.length());

//            int getByte;
//            StringBuilder getData = new StringBuilder();
//            boolean endOfStream = false;
//            while (!endOfStream) {
//                getByte = inputStream.read();
//                char c = (char) getByte;
//                getData.append(c);
//
//                if(getData.toString().contains("\r\n\r\n"))
//                    endOfStream = true;
//            }
//            // Set file intended to write to
            postContentFile = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);

            try {
                int readBytes = 0;
                byte[] buf = new byte[1024];

                FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
                int getByte;
                StringBuilder getData = new StringBuilder();
                boolean endOfStream = false;

                while (!endOfStream) {
                    getByte = inputStream.read();
                    char c = (char) getByte;
                    getData.append(c);

                    if(getData.toString().contains("\r\n\r\n"))
                        endOfStream = true;
                }
                writeBytesToFileStream(fileStream, getData, 0, readBytes);
//                do {
//                    readBytes = inputStream.read();
//
//                    if (readBytes > 0) {
//                        writeBytesToFileStream(fileStream, buf, 0, readBytes);
//                    }
//                } while (inputStream.available() > 0);
            } catch (FileNotFoundException e) {
                log.error("There was a problem with creating the file.");
            }

            responseGenerator = new ResponseGenerator(StatusCodes.CREATED, true, rootDirectory.getAbsolutePath() + "/" + requestedFile, postContentFile.length());
            generateResponseHeader();

            // Write 201 response to stream
            outputStreamWriter.write(responseHeader, 0, responseHeader.length());
            outputStreamWriter.flush();
        }catch(IOException e){
            log.error("can't write to stream : {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
    private void writeBytesToFileStream(FileOutputStream file, StringBuilder buffer, int index, int length){
        try {
            file.write(buffer.toString().getBytes(), index, length);
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
