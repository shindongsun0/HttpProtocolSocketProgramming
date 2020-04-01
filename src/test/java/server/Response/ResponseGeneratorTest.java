package server.Response;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@Slf4j
public class ResponseGeneratorTest {
    ResponseGenerator responseGenerator;

    @Test
    public void ResponseHeader_StatusCodes_200_OK() {
        ResponseGenerator responseGenerator_200 = new ResponseGenerator(StatusCodes.OK, "html", 176);
        Assert.assertEquals(responseGenerator_200.getStatusCode(), "200 OK");
        Assert.assertEquals(responseGenerator_200.getContentType(), "TEXT/html");
    }

    @Test
    public void ResponseHeader_StatusCodes_404_NOTFOUND() {
        ResponseGenerator responseGenerator_404 = new ResponseGenerator(StatusCodes.NOT_FOUND, "html", 176);
        Assert.assertEquals(responseGenerator_404.getStatusCode(), "404 Not Found");
        assertNull(responseGenerator_404.getContentType());
    }

    @Test
    public void ResponseHeader_StatusCodes_500_SERVER_ERROR() {
        ResponseGenerator responseGenerator_500 = new ResponseGenerator(StatusCodes.SERVER_ERROR, "xml", 176);
        Assert.assertEquals(responseGenerator_500.getStatusCode(), "500 Internal Server Error");
        assertNull(responseGenerator_500.getContentType());
    }

    @Test
    public void ResponseHeader_StatusCodes_201_CREATED() {
        ResponseGenerator responseGenerator_201 = new ResponseGenerator(StatusCodes.CREATED, "json", 800);
        Assert.assertEquals(responseGenerator_201.getStatusCode(), "201 Created");
        Assert.assertEquals(responseGenerator_201.getContentType(), "Application/json");
    }

    @Test
    public void ResponseHeader_StatusCodes_403_FORBIDDEN() {
        ResponseGenerator responseGenerator_403 = new ResponseGenerator(StatusCodes.FORBIDDEN);
        Assert.assertEquals(responseGenerator_403.getStatusCode(), "403 Forbidden");
        assertNull(responseGenerator_403.getContentType());
        assertFalse(responseGenerator_403.isIncludeLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void NO_ContentType_Matched_In_isContentType() {
        responseGenerator = new ResponseGenerator(StatusCodes.OK, "VND", 176);
    }

    @Test
    public void ContentType_Matched_In_isContentType() {
        responseGenerator = new ResponseGenerator(StatusCodes.OK, "HTML", 176);
        MimeType mimeType = responseGenerator.isMimeType("HTML");
        Assert.assertEquals(mimeType, MimeType.HTML);
    }
}