package client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class HttpClient {
    private static OutputStream outputStream = null;
    private static Socket connectSocket(String hostname, int port, int timeout){
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

    private static void makeRequestHeader(Socket socket, String path, String method, String body){
        try {
            outputStream = socket.getOutputStream();
            if (method.equals("GET")) {
                String request = "GET " + path + " HTTP/1.0\r\n"
                        + "Host: localhost\r\n"
                        + "Accept: */*\r\n"
                        + "Accept-Language: en-us\r\n"
                        + "Connection: close\r\n\r\n";
                outputStream.write(request.getBytes());
                outputStream.flush();
            }
            else if(method.equals("POST")){
                String request = "POST " + path + " HTTP/1.0\r\n"
                        + "Host: localhost\r\n"
                        + "Accept: */*\r\n"
                        + "Accept-Language: en=us\r\n"
                        + "Connection: close\r\n\r\n";
                outputStream.write(request.getBytes());
                outputStream.flush();
                makeRequestBodyHeader(outputStream, body);
            }
        }catch(IOException e){
            System.err.println("can't get stream connection");
        }
    }
    private static void readResponseHeader(Socket socket){
        try{
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String number;
            while ((number = bufferedReader.readLine()) != null){
                System.out.println(number);
            }
        } catch(IOException e) {
            System.err.println("cannot get inputstream connection");
        }
    }
//    private static void makeRequestBodyHeader(Socket socket, String body){
//        try{
//            outputStream = socket.getOutputStream();
//            String requestBody = body + "\r\n\r\n";
//            outputStream.write(requestBody.getBytes());
//            outputStream.flush();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }
    private static void makeRequestBodyHeader(OutputStream outputStream, String body){
        try{
            String requestBody = body + "\r\n\r\n";
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Socket socket = connectSocket("localhost", 10005,10000);
//        makeRequestHeader(socket, "mainPage/index.html", "GET", null);
        makeRequestHeader(socket, "mainPage/index.html", "POST", "hihi");
//        makeRequestBodyHeader(socket, "hihihi");
        readResponseHeader(socket);
    }
}
