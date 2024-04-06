package client.scenes;

import client.utils.ServerUtils;
import commons.dto.Event;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StartPageTest {
    @Test
    public void testInvalidInviteCode() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        assertFalse(logic.useInviteCode(""));
        assertFalse(logic.useInviteCode("MORETHANTENCHARACTERS"));
        assertFalse(logic.useInviteCode("@#$!@*^&!^@(&!^@(*!&@"));
    }

    @Test
    public void testExistingInviteCode() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        var target = new Event(0, "target", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var decoy = new Event(1, "decoy", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var list = new ArrayList<Event>();
        list.add(target);
        list.add(decoy);
        when(server.getEvents()).thenReturn(list);
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        assertTrue(logic.useInviteCode(target.getCode()));
        verify(mainCtrl).eventPage(target.getId());
    }

    @Test
    public void testNonexistentInviteCode() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        var target = new Event(0, "target", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var decoy = new Event(1, "decoy", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var list = new ArrayList<Event>();
        list.add(decoy);
        when(server.getEvents()).thenReturn(list);
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        assertFalse(logic.useInviteCode(target.getCode()));
        verify(mainCtrl, never()).eventPage(target.getId());
    }

    @Test
    public void testNewEventWithEmptyName() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        assertFalse(logic.createNewEvent(""));
    }

    @Test
    public void testNewEvent() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        Event finalEvent = new Event();
        when(server.addEvent(any())).thenAnswer(
            invocation -> {
                finalEvent.setTitle(((Event)invocation.getArgument(0)).getTitle());
                finalEvent.setId(0);
                return finalEvent;
            }
        );
        when(server.addTag(any(), anyInt())).thenAnswer(
            invocation -> {
                if (invocation.getArgument(1).equals(finalEvent.getId())) {
                    finalEvent.getTags().add(invocation.getArgument(0));
                }
                return invocation.getArgument(0);
            }
        );
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        assertTrue(logic.createNewEvent("test"));
        assertEquals("test", finalEvent.getTitle());
        assertEquals(new commons.dto.Tag(1, "food",
            255, 255, 0, finalEvent.getId()), finalEvent.getTags().getFirst());
        assertEquals(new commons.dto.Tag(2, "entrance fees",
            255, 0, 255, finalEvent.getId()), finalEvent.getTags().get(1));
        assertEquals(new commons.dto.Tag(3, "travel",
            0, 255, 255, finalEvent.getId()), finalEvent.getTags().get(2));
        verify(mainCtrl).eventPage(finalEvent.getId());
    }

    @Test
    public void testCreateLanguageTemplate() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StartPageLogic logic = new StartPageLogic(server, mainCtrl);
        StringWriter writer = new StringWriter();
        try {
            logic.createLanguageTemplate(writer);
        }
        catch (IOException e) {
            fail();
        }
        // Testing the entire file contents is a little excessive and not fun to update, so we'll just stick to
        // first few lines.
        assertTrue(writer.toString().startsWith(
            """
                # Add the name of your new language to the first line of this file as a comment!
                # Send the final version to splittyteam32@gmail.com
                add_quote = Add Quote
                cancel = Cancel
                date = Date
                total_cost = Total Cost of Event:"""
        ));
    }
}
