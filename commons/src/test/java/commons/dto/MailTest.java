package commons.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailTest {

    @Test
    void getToMail() {
        Mail mail = new Mail("a", "b", "c");
        assertEquals(mail.getToMail(), "a");
    }

    @Test
    void setToMail() {
        Mail mail = new Mail("a", "b", "c");
        mail.setToMail("aa");
        assertEquals(mail.getToMail(), "aa");
    }

    @Test
    void getSubject() {
        Mail mail = new Mail("a", "b", "c");
        assertEquals(mail.getSubject(), "b");
    }

    @Test
    void setSubject() {
        Mail mail = new Mail("a", "b", "c");
        mail.setSubject("aa");
        assertEquals(mail.getSubject(), "aa");
    }

    @Test
    void getBody() {
        Mail mail = new Mail("a", "b", "c");
        assertEquals(mail.getBody(), "c");
    }

    @Test
    void setBody() {
        Mail mail = new Mail("a", "b", "c");
        mail.setBody("aa");
        assertEquals(mail.getBody(), "aa");
    }
}