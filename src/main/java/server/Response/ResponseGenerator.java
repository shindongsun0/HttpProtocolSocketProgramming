package server.Response;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ResponseGenerator {
    private StringBuilder responseHeader;
    private String status;
    private boolean includeLocation;
    private String location;
    private long contentLength;
    private String contentType;

    public ResponseGenerator(StatusCodes statusCodes, String type, long fileContentLength) throws IllegalArgumentException{
        this.status = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        this.includeLocation = false;
        this.contentLength = fileContentLength;
        this.contentType = isContentType(type.toUpperCase()).getType();
        generateResponseHeader();
    }

    public ResponseGenerator(StatusCodes statusCodes, Socket clientSocket) throws IllegalArgumentException {
        this.status = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        generate404ResponseHeader(clientSocket);
    }

    private ContentType isContentType(String type){
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.toString().equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException :: new);
    }

    private void generateResponseHeader() {
        this.responseHeader.append("HTTP/1.1 ").append(status).append("\r\n").append("Server: localhost\r\n").append("Connection: close\r\n")
                .append("Content-Type: ").append(contentType).append("\r\n")
                .append("Content-Length: ").append(contentLength).append("\r\n")
                .append("Date: ").append(DateTimeFormat.fullDateTime()).append("\r\n")
                .append("Location: ").append(location).append("\r\n")
                .append("\r\n");
    }

    private void generate404ResponseHeader(Socket clientSocket){
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String format_time = format1.format(System.currentTimeMillis());

        this.responseHeader.append("HTTP/1.1 ")
                .append(status).append("\r\n")
                .append("Date: ").append(format_time).append("\r\n")
                .append("Connection: close\r\n")
                .append("Server: ").append(clientSocket.getInetAddress()).append(":").append(clientSocket.getPort()).append("\r\n")
                .append("\r\n");
    }
    public String getResponseHeader(){
        return responseHeader.toString();
    }


}
