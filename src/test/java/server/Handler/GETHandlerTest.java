package server.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import server.Response.ResponseGenerator;
import server.Response.StatusCodes;

import java.net.Socket;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GETHandlerTest {
    GETHandler getHandler;

    @Mock
    private Socket socket;

    @Before
    public void setUp() {

//        getHandler = new GETHandler(socket, "requestHeader",  )
//        this.responseGenerator_throw_IllegalArgumentException = new ResponseGenerator(StatusCodes.OK, "not a type of contentType", 176);
    }
    @Test
    public void 잘못된_contentType_throw_IllegalArgumentException(){

    }
    @Test
    public void handle() {
    }
}