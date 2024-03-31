package commons.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getId() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals(1,user.getId());
    }

    @Test
    void setId() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        user.setId(2);
        assertEquals(2,user.getId());
    }

    @Test
    void getName() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals("David",user.getName());
    }

    @Test
    void setName() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        user.setName("Andrei");
        assertEquals("Andrei",user.getName());
    }

    @Test
    void getEmail() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals("dgogoana@tudelft.nl",user.getEmail());
    }

    @Test
    void setEmail() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        user.setEmail("davidgogoana@tudelft.nl");
        assertEquals("davidgogoana@tudelft.nl",user.getEmail());
    }

    @Test
    void getIban() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals("54321",user.getIban());
    }

    @Test
    void setIban() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        user.setIban("12345");
        assertEquals("12345",user.getIban());
    }

    @Test
    void getBic() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals("12345",user.getBic());
    }

    @Test
    void setBic() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        user.setBic("54321");
        assertEquals("54321",user.getBic());
    }

    @Test
    void testEquals() {
        User user1 = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        User user2 = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertTrue(user1.equals(user2));
    }

    @Test
    void testHashCode() {
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345",0.0);
        assertEquals(-1646562176, user.hashCode());
    }
}