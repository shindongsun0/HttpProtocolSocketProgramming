package server.Response;

import java.net.InetAddress;
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

    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    String format_time = format.format(System.currentTimeMillis());

    public ResponseGenerator(StatusCodes statusCodes, String type, long fileContentLength) throws IllegalArgumentException{
        this.status = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        this.includeLocation = false;
        this.contentLength = fileContentLength;
        this.contentType = isContentType(type.toUpperCase()).getType();
        generateResponseHeader();
    }

    public ResponseGenerator(StatusCodes statusCodes, String requestType) throws IllegalArgumentException {
        this.status = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        generate404ResponseHeader(requestType);
    }

    private ContentType isContentType(String type){
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.toString().equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException :: new);
    }

    private void generateResponseHeader() {
        this.responseHeader.append("HTTP/1.1 ").append(status).append("\r\n")
                .append("Server: ").append(getHostAddress()).append("\r\n")
                .append("Connection: close\r\n")
                .append("Cache-control: private\r\n")
                .append("Content-Type: ").append(contentType).append("\r\n")
                .append("Content-Length: ").append(contentLength).append("\r\n")
                .append("Date: ").append(format_time).append("\r\n")
                .append("Location: ").append(location).append("\r\n")
                .append("\r\n");
    }

    private String getHostAddress(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }catch(UnknownHostException e){
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
            return e.toString();
        }
    }

    private void generate404ResponseHeader(String requestType){
        this.responseHeader.append("HTTP/1.1 ")
                .append(status).append("\r\n")
                .append("Server: ").append(getHostAddress()).append("\r\n")
                .append("Request Method: ").append(requestType).append("\r\n")
                .append("Date: ").append(format_time).append("\r\n")
                .append("Connection: close\r\n")
                .append("Cache-control: private\r\n")
                .append("\r\n");
    }

    public String getResponseHeader(){
        return responseHeader.toString();
    }


}
