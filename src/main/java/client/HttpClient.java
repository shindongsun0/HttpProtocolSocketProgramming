package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class HttpClient {
    //여기에 client socket 코드 작성
    //get 시 client는 printWriter에 http request 작성
    private Socket connectSocket(String hostname, int port, int timeout){
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            return socket;
        }catch(IOException e){
            System.out.println(e.toString());
            System.out.println(String.format("%s OCCURRED", e.getClass().getSimpleName()));
            System.out.println(Arrays.asList(e.getStackTrace()));
            return null;
        }
    }

    private void makeRequestHeader(Socket socket, String path, String method){
        try {
            OutputStream out = socket.getOutputStream();
            if (method.equals("GET")) {
                String request = "GET " + path + " HTTP/1.0\r\n"
                        + "Host: localhost\r\n"
                        + "Accept: */*\r\n"
                        + "Accept-Language: en-us\r\n"
                        + "Connection: close\r\n\r\n";
                out.write(request.getBytes());
                out.flush();
            }
        }catch(IOException e){
            System.err.println("can't get stream connection");
        }
    }
    private void readResponseHeader(Socket socket){
        try{
            InputStream in = socket.getInputStream();
            StringBuffer response = new StringBuffer();

        } catch(IOException e) {
            System.err.println("cannot get inputstream connection");
        }
    }

    public void main(String[] args){
        Socket socket = connectSocket("localhost", 10005, 5000);
        makeRequestHeader(socket, "index.html", "GET");
        readResponseHeader(socket);
    }
}
