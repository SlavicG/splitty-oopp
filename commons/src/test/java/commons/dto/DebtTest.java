package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebtTest {
    private Integer id;
    private User owed;
    private User indebted;
    private Double amount;

    private Debt d1;
    private Debt d2;
    private Debt d3;


    @BeforeEach
    void setup() {
        owed = new User(1, "Kinga", "kinga@tudelft.nl", "123", "456");
        indebted = new User(2, "Maya", "Maya@tudelft.nl", "123", "456");
        amount = 14.;
        id = 1;
        d1 = new Debt(id, owed, indebted, amount);
        d2 = new Debt(id, owed, indebted, amount);
        d3 = new Debt(id, owed, indebted, amount);
    }

    @Test
    void getId() {
        assertEquals(1, d1.getId());
    }

    @Test
    void testSetId() {
        d1.setId(2);
        assertEquals(2, d1.getId());
    }

    @Test
    void getOwed() {
        assertEquals(owed, d1.getOwed());
    }

    @Test
    void setOwed() {
        User newOwed = new User(3, "Ilker", "Ilker@tudelft.nl", "321", "987");
        d1.setOwed(newOwed);
        assertEquals(newOwed, d1.getOwed());
    }

    @Test
    void getIndebted() {
        assertEquals(indebted, d1.getIndebted());
    }

    @Test
    void setIndebted() {
        User user = new User(4, "Miruna", "miruna@tudelft.nl", "345", "178");
        d1.setIndebted(user);
        assertEquals(user, d1.getIndebted());
    }

    @Test
    void getAmount() {
        assertEquals(amount, d1.getAmount());
    }

    @Test
    void setAmount() {
        Double newAmount = 16.;
        d1.setAmount(newAmount);
        assertEquals(d1.getAmount(), newAmount);
    }

    @Test
    void testEquals() {
        assertEquals(d2, d3);
    }

    @Test
    void testHashCode() {
        assertEquals(d2.hashCode(), d3.hashCode());
    }


}