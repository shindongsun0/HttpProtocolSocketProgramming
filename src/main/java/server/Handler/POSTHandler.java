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
        requestedFile = getFile(validatePath(getPathFromHeader(requestSHeader)));
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);
        postContentFile = null;

        if (!checkIfFolderExists(locationToUpload)) {
            createDirectoryIfAbsent(locationToUpload);
        }
    }

    @Override
    public void handle() {
        File postContentFile = new File(rootDirectory.getAbsolutePath() + "/" + requestedFile);
        updatePostData(postContentFile);

        setupResponseHeader(StatusCodes.CREATED);
        sendResponseToClient(this.responseHeader);
    }

    private void setupResponseHeader(StatusCodes statusCode) {
        responseGenerator = new ResponseGenerator(statusCode, true, rootDirectory.getAbsolutePath() + "/" + requestedFile, postContentFile.length());
        this.setResponseHandler(responseGenerator.getResponseHeader());
    }

    private void updatePostData(File postContentFile) {
        try {
            FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
            String[] getData = requestSHeader.split("\r\n");
            int readBytes = getData[getData.length - 1].length();
            writeBytesToFileStream(fileStream, getData[getData.length - 1], 0, readBytes);
        } catch (FileNotFoundException e) {
            log.error("There was a problem with creating the file. {}", e.toString());
        }
    }

    private void sendResponseToClient(String responseHeader) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
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

    private boolean checkIfFolderExists(File uploadFilePath) {
        return uploadFilePath.exists();
    }

    private void createDirectoryIfAbsent(File uploadFilePath) {
        if (!uploadFilePath.mkdir()) {
            throw new SecurityException();
        }
    }
}
