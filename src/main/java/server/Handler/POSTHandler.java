package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

@Slf4j
public class POSTHandler extends HTTPHandler {
    private File postContentFile;
    private File locationToUpload;

    public POSTHandler(Socket socket, String requestHeader, File root) throws FileNotFoundException {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
        requestedFile = getFile(validatePath());
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);
        postContentFile = null;

        if (!checkIfFolderExists()) {
            createDirectoryIfAbsent();
        }
    }

    @Override
    public void handle() {
        updatePostData();
        sendResponseToClient();
    }

    private void updatePostData() {
        postContentFile = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);
        try {
            FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
            String[] getData = requestSHeader.split("\r\n");
            int readBytes = getData[getData.length - 1].length();
            writeBytesToFileStream(fileStream, getData[getData.length - 1], 0, readBytes);
        } catch (FileNotFoundException e) {
            log.error("There was a problem with creating the file. {}", e.toString());
        }
    }

    private void sendResponseToClient() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            responseGenerator = new ResponseGenerator(StatusCodes.CREATED, true, rootDirectory.getAbsolutePath() + "/" + requestedFile, postContentFile.length());
            this.setResponseHandler(responseGenerator.getResponseHeader());
            outputStreamWriter.write(responseHeader, 0, responseHeader.length());
            outputStreamWriter.flush();
        } catch (IOException e) {
            log.error("can't write to stream : {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private void writeBytesToFileStream(FileOutputStream file, String bodyData, int index, int length) {
        try {
            file.write(bodyData.getBytes(), index, length);
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private boolean checkIfFolderExists() {
        return locationToUpload.exists();
    }

    private void createDirectoryIfAbsent() {
        if (!locationToUpload.mkdir()) {
            throw new SecurityException();
        }

        // TODO 만드는 코드
    }
}
