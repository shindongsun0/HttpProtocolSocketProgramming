package server.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;

@RunWith(MockitoJUnitRunner.class)
public class GETHandlerTest {
    GETHandler getHandler;

    @Mock
    private Socket socket;

    @Before
    public void setUp() throws FileNotFoundException {
        File rootDirectory = new File(System.getProperty("user.dir"));
        getHandler = new GETHandler(socket, "requestHeader", rootDirectory);
    }

    @Test
    public void handle() {

    }
}