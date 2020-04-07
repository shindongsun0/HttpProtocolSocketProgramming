package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.net.Socket;

@RunWith(MockitoJUnitRunner.class)
public class ClientHandlerTest {
    ClientHandler clientHandler;
    @Mock
    private Socket socket;

    @Before
    public void setUp() {
        File filePath = new File(System.getProperty("user.dir"));
        clientHandler = new ClientHandler(socket, filePath);
    }

    @Test
    public void isHTTPRequest_not_include_HTTP() {
        String requestHeader = "POST hero.json /1.1";
        Assert.assertFalse(clientHandler.isHTTPRequest(requestHeader));
    }

    @Test
    public void isHTTPRequest_include_HTTP() {
        String requestHeader = "POST hero.json HTTP/1.1";
        Assert.assertTrue(clientHandler.isHTTPRequest(requestHeader));
    }
}