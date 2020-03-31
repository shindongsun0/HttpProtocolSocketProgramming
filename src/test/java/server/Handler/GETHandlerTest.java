//package server.Handler;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import server.Response.ResponseGenerator;
//import server.Response.StatusCodes;
//
//import java.io.File;
//import java.net.Socket;
//
//import static org.junit.Assert.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class GETHandlerTest {
//    GETHandler getHandler;
//
//    @Mock
//    private Socket socket;
//
//    @Before
//    public void setUp() {
//        File rootDirectory = new File(System.getProperty("user.dir"));
//        getHandler = new GETHandler(socket, "requestHeader", rootDirectory);
//    }
//
//    @Test
//    public void handle() {
//
//    }
//}