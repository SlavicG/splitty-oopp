package commons.dto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.awt.Color;
import java.io.IOException;

public class ColorDeserializer extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        int[] rgb = p.readValueAs(int[].class);
        if (rgb.length != 3) {
            throw new IllegalArgumentException("Invalid RGB array length");
        }
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
}