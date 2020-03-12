package server.Response;

public class ResponseGenerator {
    private StringBuilder responseHeader;

    public ResponseGenerator(StatusCodes statusCodes, String type, long fileContentLength) throws IllegalArgumentException{

    }

    public ResponseGenerator(StatusCodes statusCodes, boolean b, String newpage, int i) {
    }

    public String getResponseHeader(){
        return responseHeader.toString();
    }
}
