package server.Handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;


public class HTTPHandlerTest {
    public File rootDirectory = new File(System.getProperty("user.dir"));

    public String requestSHeader;

    private String getRequestSHeader(String fileName) {
        requestSHeader = "GET " + fileName + " HTTP/1.0\r\n" +
                "Host: localhost\r\n" +
                "Accept: */*\r\n" +
                "Accept-Language: en-us\r\n" +
                "Connection: close\r\n\r\n";
        return requestSHeader;
    }

    private String getWrongRequestHeader() {
        //header length < 2
        requestSHeader = "GET\r\n";
        return requestSHeader;
    }

    @Before
    public void setUp() {
        rootDirectory = new File(System.getProperty("user.dir"));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalid_filePath_throw_FileNotFoundException_In_getFile() throws FileNotFoundException {
        String fileName = "in.json";
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        File returnFile = failTest.getFile(fileName);
    }

    @Test
    public void valid_filePath_returnFile_In_getFile() throws FileNotFoundException {
        String fileName = "mainPage/hello.html";
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        File returnFile = failTest.getFile(fileName);
        Assert.assertEquals(returnFile, new File(fileName));
    }

    @Test
    public void valid_FileType_성공_In_getFileType() {
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        String fileName = "mainPage/hello.html";
        File newFile = new File(fileName);
        String input = newFile.getAbsolutePath();

        String result = failTest.getFileType(input);

        assertEquals("html", result);
    }

    @Test
    public void invalid_FileType_확장자없음_In_getFileType() {
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        String fileName = "mainPage/hello";
        File newFile = new File(fileName);
        String input = newFile.getAbsolutePath();

        String result = failTest.getFileType(input);

        assertEquals(input, result);
    }

    @Test
    public void getPathFromHeader_성공_return_path() {
        String fileName = "hello.html";
        requestSHeader = getRequestSHeader(fileName);
        HTTPImpl successTest = new HTTPImpl(requestSHeader, rootDirectory);

        String result = successTest.getPathFromHeader(requestSHeader);
        assertEquals(fileName, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getPathFromHeader_실패_IndexOutOfBoundsException() {
        requestSHeader = getWrongRequestHeader();
        HTTPImpl failTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = failTest.getPathFromHeader(requestSHeader);
    }

    @Test
    public void validatePath_성공_if_empty_path() {
        String fileName = "";
        requestSHeader = getRequestSHeader(fileName);
        HTTPImpl successTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = successTest.validatePath("");
        assertEquals("mainPage/hello.html", result);
    }

    @Test
    public void validatePath_성공_if_path_is_directory() {
        String fileName = "mainPage";
        requestSHeader = getRequestSHeader(fileName);
        HTTPImpl successTest = new HTTPImpl(requestSHeader, rootDirectory);
        String result = successTest.validatePath(fileName);
        Assert.assertEquals("mainPage/hello.html", result);
    }

    private class HTTPImpl extends HTTPHandler {
        public HTTPImpl(String header, File file) {
            this.requestSHeader = header;
            this.rootDirectory = file;
        }

        @Override
        public void handle() {

        }
    }
}