package server.Response;

import jdk.net.SocketFlow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class ResponseGeneratorTest {
    private ResponseGenerator responseGenerator_200, responseGenerator_404, responseGenerator_500, responseGenerator_302, responseGenerator_100, responseGenerator_201, responseGenerator_403;
    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    String format_time = format.format(System.currentTimeMillis());

    @Before
    public void setUp(){
        this.responseGenerator_200 = new ResponseGenerator(StatusCodes.OK, "HTML", 176);
        this.responseGenerator_404 = new ResponseGenerator(StatusCodes.NOT_FOUND, "GET");

        this.responseGenerator_500 = new ResponseGenerator(StatusCodes.SERVER_ERROR, "GET");
        this.responseGenerator_302 = new ResponseGenerator(StatusCodes.FOUND, "GET");
        this.responseGenerator_201 = new ResponseGenerator(StatusCodes.CREATED, "GET");
        this.responseGenerator_100 = new ResponseGenerator(StatusCodes.CONTINUE, "GET");
        this.responseGenerator_403 = new ResponseGenerator(StatusCodes.FORBIDDEN, "GET");
    }

    @Test
    public void ResponseHeader_StatusCodes_OK() {
        assertEquals("HTTP/1.1 200 OK\r\n" +
                "Server: 192.168.0.125\r\n" +
                "Connection: close\r\n" +
                "Cache-control: private\r\n" +
                "Content-Type: TEXT/html\r\n" +
                "Content-Length: 176\r\n" +
                "Date: " + format_time + "\r\n" +
                "Location: null\r\n" + "\r\n"
                , responseGenerator_200.getResponseHeader());
    }
    @Test
    public void ResponseHeader_StatusCodes_NOTFOUND(){
        assertEquals(
                "HTTP/1.1 404 Not Found\r\n" +
                        "Server: 192.168.0.125\r\n" +
                        "Request Method: GET\r\n" +
                        "Date: " + format_time + "\r\n" +
                        "Connection: close\r\n" +
                        "Cache-control: private\r\n" + "\r\n"
                , responseGenerator_404.getResponseHeader());
    }



}