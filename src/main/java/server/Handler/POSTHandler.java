package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class POSTHandler extends HTTPHandler {
    private File postContentFile;
    private File locationToUpload;

    public POSTHandler(Socket socket, String requestHeader, File root) throws FileNotFoundException {
        clientSocket = socket;
        requestSHeader = requestHeader;
        rootDirectory = root;
//        requestedFile = getFile(validatePath(getPathFromHeader(requestSHeader)));
        requestedFile = new File(validatePath(getPathFromHeader(requestSHeader)));
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/mainPage/post");
        if (!checkIfFolderExists(locationToUpload)) {
            createDirectoryIfAbsent(locationToUpload);
        }
    }

    @Override
    public void handle() {
        String splitFile[] = requestedFile.toString().split("\\.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = splitFile[0] + "_" + dateFormat.format(new Date()) + "." + splitFile[1];
        postContentFile = new File(locationToUpload + "/" + fileName);
        updatePostData(postContentFile);

        setupResponseHeader(StatusCodes.CREATED);
        sendResponseToClient(this.responseHeader);
    }

    private void setupResponseHeader(StatusCodes statusCode) {
        responseGenerator = new ResponseGenerator(statusCode, true, postContentFile.toString(), postContentFile.length());
        this.setResponseHandler(responseGenerator.getResponseHeader());
    }

    private void updatePostData(File postContentFile) {
        try {
            FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
            String[] getData = requestSHeader.split("\r\n");
            int startOfBody = findStartOfBody(getData);
            for (int i = startOfBody; i < getData.length; i++) {
                writeBytesToFileStream(fileStream, getData[i], 0, getData[i].length());
            }
        } catch (FileNotFoundException e) {
            log.error("There was a problem with creating the file. {}", e.toString());
        }
    }

    private int findStartOfBody(String[] getData) {
        int endOfHeader = 0;
        for (int i = getData.length - 1; i >= 0; i--) {
            if (!getData[i].equals(" "))
                endOfHeader = i;
            else break;
        }
        return endOfHeader;
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
