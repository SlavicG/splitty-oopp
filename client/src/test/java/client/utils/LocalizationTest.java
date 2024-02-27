package client.utils;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationTest {
    @Test
    public void testEnglish() {
        ResourceBundle bundle = ResourceBundle.getBundle("client.language", Locale.forLanguageTag("en"));
        assertEquals(bundle.getString("cancel"), "Cancel");
    }

    @Test
    public void testDutch() {
        ResourceBundle bundle = ResourceBundle.getBundle("client.language", Locale.forLanguageTag("nl"));
        assertEquals(bundle.getString("cancel"), "Annuleren");
    }
}
