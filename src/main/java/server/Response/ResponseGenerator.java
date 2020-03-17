package server.Response;

public class ResponseGenerator {
    private StringBuilder responseHeader;
    private String status;
    private boolean includeLocation;
    private String location;
    private long contentLength;
    private String type;

    public ResponseGenerator(StatusCodes statusCodes, String type, long fileContentLength) throws IllegalArgumentException{
        this.status = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        this.includeLocation = false;
        this.contentLength = fileContentLength;
        this.type = isContentType(type);
        generateResponseHeader();
    }

    private String isContentType(String type) throws IllegalArgumentException{
        if(type.equals("text/html"))
            return type;
        else
            throw new IllegalArgumentException();
    }

    private void generateResponseHeader() {
        this.responseHeader.append("HTTP/1.1 ").append(status).append("\n").append("Server: localhost\n").append("Connection: close\n")
                .append("Content-Type: ").append("text/html\n")
                .append("Content-Length: ").append(contentLength)
                .append("Location: ").append(location)
                .append("\r\n\r\n");
    }

    public ResponseGenerator(StatusCodes statusCodes, boolean b, String newpage, long i) {
        this.status = statusCodes.getStatus();
        this.type = null;
        this.includeLocation = b;
        this.location = newpage;
        this.responseHeader = new StringBuilder();
        this.contentLength = i;
    }

    public String getResponseHeader(){
        return responseHeader.toString();
    }


}
