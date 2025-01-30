package co.edu.eci.arep;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {

    @Test
    public void testHelloRestService() {
        String response = HttpServer.helloRestService("/app/hello", "name=Juan");
        assertTrue(response.contains("\"message\":\"El usuario Juan no está registrado.\""));
    }

    @Test
    public void testPostRestService() {
        String response = HttpServer.postRestService("/app/hello", "name=Pedro");
        assertTrue(response.contains("\"message\":\"Usuario Pedro registrado correctamente.\""));
    }
}
