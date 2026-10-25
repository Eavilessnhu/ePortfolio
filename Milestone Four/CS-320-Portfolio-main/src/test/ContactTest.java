package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Contact;

class ContactTest {

    private Contact contact;

    @BeforeEach
    void setUp() {
        // Uses the required parameterized constructor since the default constructor was removed
        contact = new Contact("0987654321", "Elias", "Aviles", "941 Maple Ave", "7177159602");
    }

    @Test
    void testContactCreationSuccess() {
        assertEquals("0987654321", contact.getContactID());
        assertEquals("Elias", contact.getFirstName());
        assertEquals("Aviles", contact.getLastName());
        assertEquals("941 Maple Ave", contact.getAddress());
        assertEquals("7177159602", contact.getPhoneNum());
    }

    @Test
    void testInvalidContactIdThrowsException() {
        // ID > 10 characters
        assertThrows(IllegalArgumentException.class, () -> 
            new Contact("09876543210", "Elias", "Aviles", "941 Maple Ave", "7177159602")
        );

        // ID is null
        assertThrows(IllegalArgumentException.class, () -> 
            new Contact(null, "Elias", "Aviles", "941 Maple Ave", "7177159602")
        );
    }

    @Test
    void testSetFirstNameValidation() {
        // Valid update
        contact.setFirstName("Daniel");
        assertEquals("Daniel", contact.getFirstName());

        // > 10 characters
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setFirstName("ChristopherLong")
        );

        // Null check
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setFirstName(null)
        );
    }

    @Test
    void testSetLastNameValidation() {
        // Valid update
        contact.setLastName("Ortiz");
        assertEquals("Ortiz", contact.getLastName());

        // > 10 characters
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setLastName("ExcessiveLengthLastName")
        );

        // Null check
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setLastName(null)
        );
    }

    @Test
    void testSetAddressValidation() {
        // Valid update
        contact.setAddress("101 North Pointe Blvd");
        assertEquals("101 North Pointe Blvd", contact.getAddress());

        // > 30 characters
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setAddress("12345 Extremely Long Highway Roadway Lane Boulevard Suite 500")
        );

        // Null check
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setAddress(null)
        );
    }

    @Test
    void testSetPhoneNumValidation() {
        // Valid update
        contact.setPhoneNum("7172939293");
        assertEquals("7172939293", contact.getPhoneNum());

        // > 10 characters
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setPhoneNum("7172939293000")
        );

        // Null check
        assertThrows(IllegalArgumentException.class, () -> 
            contact.setPhoneNum(null)
        );
    }
}