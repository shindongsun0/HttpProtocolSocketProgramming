package server;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.net.Socket;

@RunWith(MockitoJUnitRunner.class)
public class ClientHandlerTest {

    @Mock
    private Socket socket;

    @Before
    public void setUp() {
        File filePath = new File(System.getProperty("user.dir"));
        ClientHandler clientHandler = new ClientHandler(socket, filePath);
    }

    @Test
    public void run_readResponseHeader() {

    }

    @Test
    public void readResponseHeader() {
    }

    @Test
    public void isHTTPRequest_not_include_HTTP() {

    }

    @Test
    public void mappingHandler() {
    }
}