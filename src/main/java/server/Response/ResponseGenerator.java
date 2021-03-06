package server.Response;

import lombok.AccessLevel;
import lombok.Getter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Getter
public class ResponseGenerator {
    private StringBuilder responseHeader;
    private String statusCode;
    private boolean includeLocation;
    private String location;
    private long contentLength;
    private String contentType;

    @Getter(AccessLevel.NONE)
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Getter(AccessLevel.NONE)
    String format_time = format.format(System.currentTimeMillis());

    public ResponseGenerator(StatusCodes statusCodes, String type, long fileLength) throws IllegalArgumentException {
        this.statusCode = statusCodes.getStatus();
        this.responseHeader = new StringBuilder();
        this.includeLocation = false;
        this.contentLength = fileLength;
        this.contentType = isMimeType(type.toUpperCase()).getType();
        buildResponseHeader();
    }

    public ResponseGenerator(StatusCodes statusCodes) {
        this.statusCode = statusCodes.getStatus();
        this.contentType = null;
        this.includeLocation = false;
        this.location = null;
        this.responseHeader = new StringBuilder();
        this.contentLength = 0;
        buildResponseHeader();
    }

    //http created
    public ResponseGenerator(StatusCodes statusCodes, boolean includeLocation, String location, long fileLength) {
        this.statusCode = statusCodes.getStatus();
        this.contentType = null;
        this.includeLocation = includeLocation;
        this.location = location;
        this.responseHeader = new StringBuilder();
        this.contentLength = fileLength;
        buildResponseHeader();
    }

    protected MimeType isMimeType(String type) {
        return Arrays.stream(MimeType.values())
                .filter(mimeType -> mimeType.toString().equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void buildResponseHeader() {
        this.responseHeader.append("HTTP/1.1 ").append(statusCode).append("\r\n")
                .append("Server: ").append(getHostAddress()).append("\r\n")
                .append("Connection: close\r\n")
                .append("Cache-control: private\r\n")
                .append("Date: ").append(format_time).append("\r\n");
        if (contentType != null) {
            this.responseHeader.append("Content-Type: ").append(contentType).append("\r\n");
        }
        if (contentLength != 0) {
            this.responseHeader.append("Content-Length: ").append(contentLength).append("\r\n");
        }
        if (includeLocation) {
            this.responseHeader.append("Location: ").append(location).append("\r\n");
        }
        this.responseHeader.append("\r\n");
    }

    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e.toString());
            System.out.println(Arrays.asList(e.getStackTrace()));
            return e.toString();
        }
    }

    public String getResponseHeader() {
        return responseHeader.toString();
    }
}
