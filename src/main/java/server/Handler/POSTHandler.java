package server.Handler;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        this.requestHeader = requestHeader;
        rootDirectory = root;
        requestedFile = new File(validatePath(getPathFromHeader(this.requestHeader)));
        locationToUpload = new File(rootDirectory.getAbsolutePath() + "/mainPage/post");
        if (!checkIfFolderExists(locationToUpload)) {
            createDirectoryIfAbsent(locationToUpload);
        }
    }

    @Override
    public void handle() {

        String[] splitFile = requestedFile.toString().split("\\.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName;
        try {
            fileName = splitFile[0] + "_" + dateFormat.format(new Date()) + "." + splitFile[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException();
        }
        updatePostData(fileName, splitFile[1]);
        setupResponseHeader(StatusCodes.CREATED);
        sendResponseToClient(this.responseHeader);
    }

    private void setupResponseHeader(StatusCodes statusCode) {
        responseGenerator = new ResponseGenerator(statusCode, true, postContentFile.toString(), postContentFile.length());
        this.setResponseHandler(responseGenerator.getResponseHeader());
    }

    public void isJsonString(int startOfBody, String[] getData) {
        try {
            StringBuffer jsonData = new StringBuffer();
            for (int i = startOfBody; i < getData.length; i++) {
                jsonData.append(getData[i]);
            }
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(String.valueOf(jsonData));
        } catch (ParseException e) {
            log.error("is not a json string! {}", e.toString());
            throw new IllegalArgumentException(requestHeader + "'s body is not valid JSON string");
        }
    }

    private void updatePostData(String fileName, String fileType) {
        try {
            String[] getData = requestHeader.split("\r\n");
            int startOfBody = findStartOfBody(getData);
            if (fileType.equals("json"))
                isJsonString(startOfBody, getData);

            postContentFile = new File(locationToUpload + "/" + fileName);
            FileOutputStream fileStream = new FileOutputStream(postContentFile, true);
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
            if (!getData[i].equals(" ")) {
                endOfHeader = i;
            } else break;
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
