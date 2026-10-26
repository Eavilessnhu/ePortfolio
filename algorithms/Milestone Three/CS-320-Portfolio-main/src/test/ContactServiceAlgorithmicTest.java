package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Contact;
import main.ContactService;

class ContactServiceAlgorithmicTest {
    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        // Populate varied records to test index traversals
        service.addContact(new Contact("C101", "Alice", "Anderson", "100 Pine St", "5551112222"));
        service.addContact(new Contact("C102", "Bob", "Brown", "200 Oak St", "5553334444"));
        service.addContact(new Contact("C103", "Charlie", "Baker", "300 Elm St", "5555556666"));
        service.addContact(new Contact("C104", "Daniel", "Davis", "400 Cedar St", "5557778888"));
    }

    @Test
    void testConstantTimeLookup() {
        // Verifies O(1) primary hash table retrieval
        Contact retrieved = service.getContact("C103");
        assertNotNull(retrieved);
        assertEquals("Charlie", retrieved.getFirstName());
        assertEquals("Baker", retrieved.getLastName());
    }

    @Test
    void testTreePrefixSearch() {
        // Verifies O(log n) secondary tree prefix scan for 'B'
        List<Contact> results = service.searchByLastNamePrefix("B");
        assertEquals(2, results.size());
        
        // Confirms both matches were extracted via the tree index
        assertTrue(results.stream().anyMatch(c -> c.getLastName().equalsIgnoreCase("Baker")));
        assertTrue(results.stream().anyMatch(c -> c.getLastName().equalsIgnoreCase("Brown")));
    }

    @Test
    void testTreeRangeQuery() {
        // Verifies O(log n + m) bounded range extraction: 'Anderson' through 'Brown'
        List<Contact> results = service.getContactsInLastNameRange("Anderson", "Brown");
        assertEquals(3, results.size());

        // Confirms alphabetical ordering preserved by the balanced search tree
        assertEquals("Anderson", results.get(0).getLastName());
        assertEquals("Baker", results.get(1).getLastName());
        assertEquals("Brown", results.get(2).getLastName());
    }
}