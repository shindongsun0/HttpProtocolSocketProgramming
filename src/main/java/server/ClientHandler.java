package server;


import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private static final String DEFAULT_FILE_PATH = "index.html";

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        InetAddress inetAddress = clientSocket.getInetAddress();
        System.out.println(inetAddress.getHostAddress() + "connected to Server");
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + "started!!");
            readResponse();
        } catch (IOException e) {
            System.out.println(Arrays.asList(e.getStackTrace()));
        }
    }

    private void readResponse() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        BufferedReader request = new BufferedReader(new InputStreamReader(inputStream));

        OutputStream outputStream = clientSocket.getOutputStream();
        PrintWriter response = new PrintWriter(new OutputStreamWriter(outputStream), true);

        String requestMessageLine = request.readLine();
        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

        if (tokenizedLine.nextToken().equals("GET")) {
            String fileName = tokenizedLine.nextToken();
            if (fileName.startsWith("/")) {
                if (fileName.length() > 1)
                    fileName = fileName.substring(1);
                else
                    fileName = DEFAULT_FILE_PATH;
            }
            File file = new File(fileName);
            if (file.exists()) {
                String mimeType = new MimetypesFileTypeMap().getContentType(file);
                int numOfBytes = (int) file.length();

                FileInputStream readFile = new FileInputStream(fileName);
                byte[] fileInBytes = new byte[numOfBytes];
                readFile.read(fileInBytes);

                response.println("HTTP/1.0 200 Document Follows");
                response.println("Content-Type: " + mimeType);

                response.println("Content-Length: " + numOfBytes);
                response.println();

                response.write(String.valueOf(fileInBytes), 0, numOfBytes);
            } else {
                System.out.println("Bad Request");
                response.println("HTTP/1.0 404 Not Found");
                response.println("Connection: close");
                response.println();
            }
        } else {
            // 잘못된 요청임을 나타내는 400 에러를 출력하고 접속을 종료한다.
            System.out.println("Bad Request");

            response.println("HTTP/1.0 400 Bad Request Message");
            response.println("Connection: close");
            response.println();
        }

        clientSocket.close();
        System.out.println("Connection Closed");
    }

}