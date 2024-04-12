package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InvitationPageCtrlTest {

    @Test
    public void IsValidEmailTest() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        MailPageLogic logic = new MailPageLogic(server,mainCtrl);
        assertTrue(logic.isValidEmail("Hello@gmail.com"));
    }

    @Test
    public void IsNotValidEmailTest() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        MailPageLogic logic = new MailPageLogic(server,mainCtrl);
        assertFalse(logic.isValidEmail("HelloGmail.com"));
    }
}