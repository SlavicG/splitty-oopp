package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartPageLogic {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public StartPageLogic(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public boolean useInviteCode(String code) {
        if (code.length() != 10) {
            return false;
        }
        for(int i = 0; i < 10; ++i)
            if (code.charAt(i) < 'A' || code.charAt(i) > 'Z') {
                return false;
            }
        Integer eventId = Event.getIdFromCode(code);
        List<Integer> ids = server.getEvents().stream().map(Event::getId).toList();
        if (!ids.contains(eventId)) {
            return false;
        }
        mainCtrl.eventPage(eventId);
        return true;
    }

    public boolean createNewEvent(String name) {
        // Make sure the event name isn't blank.
        // This is probably better checked server-side, but this will do for now.
        if (name.isEmpty()) {
            return false;
        }

        Event event = new Event();
        event.setTitle(name);
        Event finalEvent = server.addEvent(event);
        server.addTag(new commons.dto.Tag(1, "food",
            255, 255, 0, finalEvent.getId()), finalEvent.getId());
        server.addTag(new commons.dto.Tag(2, "entrance fees",
            255, 0, 255, finalEvent.getId()), finalEvent.getId());
        server.addTag(new commons.dto.Tag(3, "travel",
            0, 255, 255, finalEvent.getId()), finalEvent.getId());
        mainCtrl.eventPage(finalEvent.getId());

        return true;
    }

    public String createLanguageTemplate() throws IOException {
        String path = String.format("%s/Downloads/template.properties", System.getProperty("user.home"));
        createLanguageTemplate(new FileWriter(path));
        return path;
    }

    public void createLanguageTemplate(Writer writer) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("client.language", Locale.forLanguageTag("en"));
        writer.write("# Add the name of your new language to the first line of this file as a comment!\n"+
            "# Send the final version to splittyteam32@gmail.com\n");
        for (String key : bundle.keySet()) {
            writer.write(String.format("%s = %s\n", key, bundle.getString(key)));
        }
        writer.flush();
    }
}
