package client.utils;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @Test
    public void testCustomReader() {
        Configuration configuration = new Configuration(new StringReader("server.url = this is NOT a url..."));
        assertEquals("this is NOT a url...", configuration.getServerURL());
    }

    @Test
    public void testDefaultConfig() {
        Configuration configuration = new Configuration();
        assertEquals("http://localhost:8080/", configuration.getServerURL());
    }

    @Test
    public void testReadFailure() {
        Configuration configuration = new Configuration(null);
        assertEquals("http://localhost:8080/", configuration.getServerURL());
    }
}
