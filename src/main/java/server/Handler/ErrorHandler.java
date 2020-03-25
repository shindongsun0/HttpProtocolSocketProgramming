package server.Handler;

import java.io.File;
import java.net.Socket;

public class ErrorHandler extends HTTPHandler{
    public ErrorHandler(Socket socket, String requestHeader, File root) {
        super();
    }

    public ErrorHandler(Socket socket, String requestHeader, File root, Object serverError) {
        super();
    }

    @Override
    public void handle() {

    }
}
