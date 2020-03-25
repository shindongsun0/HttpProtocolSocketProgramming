package server.Response;

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
        this.contentType = isContentType(type);
        generateResponseHeader();
    }

    private String isContentType(String type) throws IllegalArgumentException{
        type = type.toUpperCase();

        String finalType = type;
        ContentType foundType = Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.toString().equals(finalType))
                .findAny()
                .orElse(null);

        if(foundType != null)
            return foundType.getType();
        else
            throw new IllegalArgumentException();
    }

    private void generateResponseHeader() {
        this.responseHeader.append("HTTP/1.1 ").append(status).append("\r\n").append("Server: localhost\r\n").append("Connection: close\r\n")
                .append("Content-Type: ").append(contentType).append("\r\n")
                .append("Content-Length: ").append(contentLength).append("\r\n")
                .append("Location: ").append(location).append("\r\n")
                .append("\r\n");
    }

    public ResponseGenerator(StatusCodes statusCodes, boolean b, String newpage, long i) {
        this.status = statusCodes.getStatus();
        this.contentType = null;
        this.includeLocation = b;
        this.location = newpage;
        this.responseHeader = new StringBuilder();
        this.contentLength = i;
    }

    public String getResponseHeader(){
        return responseHeader.toString();
    }


}
