package server.Response;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    @Test
    public void ResponseHeader_StatusCodes_OK() {
        ResponseGenerator responseGenerator_200 = new ResponseGenerator(StatusCodes.OK, "html", 176);
        Assert.assertEquals(responseGenerator_200.getStatus(), "200 OK");
        Assert.assertEquals(responseGenerator_200.getContentType(), "TEXT/html");
    }
    @Test
    public void ResponseHeader_StatusCodes_NOTFOUND(){
        ResponseGenerator responseGenerator_404 = new ResponseGenerator(StatusCodes.NOT_FOUND, "GET");
        Assert.assertEquals(responseGenerator_404.getStatus(), "404 Not Found");
        assertNull(responseGenerator_404.getContentType());
    }

    @Test
    public void ResponseHeader_StatusCodes_500(){
        ResponseGenerator responseGenerator_500 = new ResponseGenerator(StatusCodes.SERVER_ERROR, "GET");
        Assert.assertEquals(responseGenerator_500.getStatus(), "500 Internal Server Error");
        assertNull(responseGenerator_500.getContentType());
    }

    @Test
    public void ResponseHeader_StatusCodes_302(){
        ResponseGenerator responseGenerator_302 = new ResponseGenerator(StatusCodes.FOUND, "html", 176);

    }

    @Test
    public void ResponseHeader_StatusCodes_403(){
        ResponseGenerator responseGenerator_403 = new ResponseGenerator(StatusCodes.FORBIDDEN, "GET");
        Assert.assertEquals(responseGenerator_403.getStatus(), "403 Forbidden");
        assertNull(responseGenerator_403.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void NO_ContentType_Matched_In_isContentType(){
         responseGenerator = new ResponseGenerator(StatusCodes.OK, "VND", 176);
    }

    @Test
    public void ContentType_Matched_In_isContentType(){
        responseGenerator = new ResponseGenerator(StatusCodes.OK, "HTML", 176);
        ContentType contentType = responseGenerator.isContentType("HTML");
        Assert.assertEquals(contentType, ContentType.HTML);
    }



}