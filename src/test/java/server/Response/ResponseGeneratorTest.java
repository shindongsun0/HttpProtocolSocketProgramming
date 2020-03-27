package server.Response;

import jdk.net.SocketFlow;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.AbstractDocument;
import javax.xml.ws.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

@Slf4j
public class ResponseGeneratorTest {
    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    String format_time = format.format(System.currentTimeMillis());
    ResponseGenerator responseGenerator;

    @Before
    public void setUp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format_time = format.format(System.currentTimeMillis());

//        this.responseGenerator_302 = new ResponseGenerator(StatusCodes.FOUND, "GET");
//        this.responseGenerator_201 = new ResponseGenerator(StatusCodes.CREATED, "GET");
//        this.responseGenerator_100 = new ResponseGenerator(StatusCodes.CONTINUE, "GET");
//        this.responseGenerator_403 = new ResponseGenerator(StatusCodes.FORBIDDEN, "GET");
    }



//    @Test
//    public void ResponseHeader_StatusCodes_OK() {
//        ResponseGenerator responseGenerator_200 = new ResponseGenerator(StatusCodes.OK, "html", 176);
//        Assert.assertEquals(responseGenerator_200.getStatus(), "200 OK");
//        Assert.assertEquals(responseGenerator_200.getContentType(), "TEXT/html");
//    }
    @Test
    public void ResponseHeader_StatusCodes_NOTFOUND(){
        ResponseGenerator responseGenerator_404 = new ResponseGenerator(StatusCodes.NOT_FOUND, "GET");
        assertEquals(
                "HTTP/1.1 404 Not Found\r\n" +
                        "Server: 192.168.0.125\r\n" +
                        "Request Method: GET\r\n" +
                        "Date: " + format_time + "\r\n" +
                        "Connection: close\r\n" +
                        "Cache-control: private\r\n" + "\r\n"
                , responseGenerator_404.getResponseHeader());
    }

    @Test
    public void ResponseHeader_StatusCodes_500(){
        ResponseGenerator responseGenerator_500 = new ResponseGenerator(StatusCodes.SERVER_ERROR, "GET");
        assertEquals(
                "HTTP/1.1 500 Internal Server Error\r\n" +
                "Server: 192.168.0.125\r\n" +
                "Request Method: GET\r\n" +
                "Date: " + format_time + "\r\n" +
                "Connection: close\r\n" +
                "Cache-control: private\r\n" + "\r\n"
                ,responseGenerator_500.getResponseHeader());
    }

    @Test
    public void ResponseHeader_StatusCodes_302(){
        ResponseGenerator responseGenerator_302 = new ResponseGenerator(StatusCodes.FOUND, "html", 176);

    }

    @Test(expected = IllegalArgumentException.class)
    public void NO_ContentType_Matched_In_isContentType(){
         responseGenerator = new ResponseGenerator(StatusCodes.OK, "VND", 176);
    }

    @Test
    public void ContentType_Matched_In_isContentType(){
        responseGenerator = new ResponseGenerator(StatusCodes.OK, "HTML", 176);
        ContentType contentType = responseGenerator.isContentType("HTML");
        assertEquals(contentType, ContentType.HTML);

    }



}