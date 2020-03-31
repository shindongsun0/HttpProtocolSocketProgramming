package client;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

@Slf4j
public class HttpClient {
    private static OutputStream outputStream = null;

    private static Socket connectSocket(String hostname, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), timeout);
        return socket;
    }

    private static void makeRequestHeader(Socket socket, String path, String method, String body) {
        try {
            outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            if (method.equals("GET")) {
                String request = "GET " + path + " HTTP/1.0\r\n"
                        + "Host: localhost\r\n"
                        + "Accept: */*\r\n"
                        + "Accept-Language: en-us\r\n"
                        + "Connection: close\r\n\r\n";
                outputStream.write(request.getBytes());
                outputStream.flush();
            } else if (method.equals("POST")) {
                printWriter.println("POST " + path + " HTTP/1.0");
                printWriter.println("Host: localhost");
                printWriter.println("Accept: */*");
                printWriter.println("Accept-Language: en-us");
                printWriter.println("Connection: close");
                printWriter.println(" ");
                printWriter.println(body);
                printWriter.println();

                printWriter.flush();
            }
        } catch (IOException e) {
            log.error("can't open stream {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private static void readResponseHeader(Socket socket) {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String number;
            while ((number = bufferedReader.readLine()) != null) {
                System.out.println(number);
            }
        } catch (IOException e) {
            System.err.println("cannot get inputstream connection");
        } finally {
            try {
                outputStream.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("can't close stream {}", e.toString());
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("cannot close socket {}", e.toString());
        }
    }

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = connectSocket("localhost", 10005, 10000);
        } catch (IOException e) {
            log.error("can't connect to Socket {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        makeRequestHeader(socket, "mainPage/hello.txt", "GET", null);
//        makeRequestHeader(socket, "mainPage/hello.txt", "POST", "hihi");
        readResponseHeader(socket);
        closeSocket(socket);
    }
}
