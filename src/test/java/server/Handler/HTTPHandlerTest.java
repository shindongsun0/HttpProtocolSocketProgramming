package server.Handler;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;


public class HTTPHandlerTest {
    public File rootDirectory = new File(System.getProperty("user.dir"));

    public String requestSHeader;

    private String getRequestSHeader(String fileName){
        requestSHeader = "GET " + fileName + " HTTP/1.0\r\n" +
                "Host: localhost\r\n" +
                "Accept: */*\r\n" +
                "Accept-Language: en-us\r\n" +
                "Connection: close\r\n\r\n";
        return requestSHeader;
    }

    private String getWrongrequestHeader(){
        //header length < 2
        requestSHeader = "GET\r\n";
        return requestSHeader;
    }

    @Before
    public void setUp(){
        rootDirectory =  new File(System.getProperty("user.dir"));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalid_filePath_throw_FileNotFoundException_In_getFile() throws FileNotFoundException {
        String fileName = "in.json";
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        File returnFile = failTest.getFile(fileName);
    }
    @Test
    public void valid_filePath_returnFile_In_getFile() throws FileNotFoundException {
        String fileName = "mainPage/index.html";
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        File returnFile = failTest.getFile(fileName);
    }

    @Test
    public void valid_FileType_성공_In_getFileType() {
        requestSHeader = getRequestSHeader("mainPage/index.html");
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);

        File newFile = new File("mainPage/index.html");
        String input = newFile.getAbsolutePath();

        String result = failTest.getFileType(input);

        assertEquals("html", result);
    }
    @Test
    public void invalid_FileType_실패_In_getFileType() {
        requestSHeader = getRequestSHeader("mainPage/index");
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);

        File newFile = new File("mainPage/index");
        String input = newFile.getAbsolutePath();

        String result = failTest.getFileType(input);

        assertEquals(input, result);
    }



    @Test
    public void getPathFromHeader_성공_return_path() {
        requestSHeader = getRequestSHeader("index.html");
        HTTPImpl successTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = successTest.getPathFromHeader();
        assertEquals("index.html", result);
    }
    @Test
    public void getPathFromHeader_성공_if_empty_path(){
        requestSHeader = getRequestSHeader("");
        HTTPImpl successTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = successTest.getPathFromHeader();
        assertEquals("mainPage/index.html", result);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void getPathFromHeader_실패_IndexOutOfBoundsException(){
        requestSHeader = getWrongrequestHeader();
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = failTest.getPathFromHeader();
    }

    private class HTTPImpl extends HTTPHandler{
        public HTTPImpl(String header, File file){
            this.requestSHeader = header;
            this.rootDirectory = file;
        }
        @Override
        public void handle() {

        }
    }
}