package client.utils;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @Test
    public void testCustomReader() {
        Configuration configuration = new Configuration(new StringReader("server.url = this is NOT a url...\n" +
                "language.locale = nl"));
        assertEquals("this is NOT a url...", configuration.getBaseServerUrl());
        assertEquals(Locale.forLanguageTag("nl"), configuration.getLocale());
    }

    @Test
    public void testDefaultConfig() {
        Configuration configuration = new Configuration();
        assertEquals("localhost:8080", configuration.getBaseServerUrl());
        assertEquals(Locale.forLanguageTag("en"), configuration.getLocale());
    }

    @Test
    public void testReadFailure() {
        Configuration configuration = new Configuration(null);
        assertEquals("localhost:8080", configuration.getBaseServerUrl());
    }

    @Test
    public void testServerUrls() {
        Configuration configuration = new Configuration(new StringReader("server.url = localhost:2020"));
        assertEquals("localhost:2020", configuration.getBaseServerUrl());
        assertEquals("ws://localhost:2020/websocket", configuration.getWebsocketServerUrl());
        assertEquals("http://localhost:2020/", configuration.getHttpServerUrl());
    }
}
