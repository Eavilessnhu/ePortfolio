package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Contact;
import main.MongoContactRepository;

class MongoContactRepositoryTest {

    // Using your Atlas credentials with a 5-second connection timeout:
    // (Replace <your-cluster-address> with the host shown in your MongoDB VS Code extension connection)
    private static final String CONNECTION_URI = 
        "mongodb+srv://eliavi649_db_user:M08Ct8PIun2KvwIo@cluster0.1rzafmp.mongodb.net";
    
    private static final String TEST_DB = "contact_test_db";
    private static final String TEST_COLLECTION = "contacts";

    private MongoContactRepository repository;

    @BeforeAll
    static void disableDriverLogs() {
        // Suppresses verbose MongoDB driver warning and info logging
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
    }

    @BeforeEach
    void setUp() {
        repository = new MongoContactRepository(CONNECTION_URI, TEST_DB, TEST_COLLECTION);

        // Wipe old records so test runs remain isolated and repeatable
        for (Contact c : repository.findAll()) {
            repository.deleteById(c.getContactID());
        }

        // Seed initial documents to test traversals and lookups
        repository.save(new Contact("C101", "Alice", "Anderson", "100 Pine St", "5551112222"));
        repository.save(new Contact("C102", "Bob", "Brown", "200 Oak St", "5553334444"));
        repository.save(new Contact("C103", "Charlie", "Baker", "300 Elm St", "5555556666"));
    }

    @AfterEach
    void tearDown() {
        // Cleanup after tests complete
        if (repository != null) {
            for (Contact c : repository.findAll()) {
                repository.deleteById(c.getContactID());
            }
        }
    }

    @Test
    void testFindById() {
        Optional<Contact> result = repository.findById("C101");
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        assertEquals("Anderson", result.get().getLastName());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Contact> result = repository.findById("DOES_NOT_EXIST");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveUpdatesExistingRecord() {
        // Verifies upsert behavior: same ID replaces fields rather than creating a duplicate
        Contact updated = new Contact("C101", "Alicia", "Anderson", "999 Elm Ave", "5551112222");
        repository.save(updated);

        Optional<Contact> result = repository.findById("C101");
        assertTrue(result.isPresent());
        assertEquals("Alicia", result.get().getFirstName());
        assertEquals("999 Elm Ave", result.get().getAddress());
    }

    @Test
    void testDeleteById() {
        assertTrue(repository.deleteById("C102"));
        assertFalse(repository.existsById("C102"));
        assertTrue(repository.findById("C102").isEmpty());
    }

    @Test
    void testFindByLastNamePrefix() {
        // Verifies the secondary B-Tree index prefix query for last names starting with 'B'
        List<Contact> results = repository.findByLastNamePrefix("B");
        assertEquals(2, results.size());

        assertTrue(results.stream().anyMatch(c -> c.getLastName().equalsIgnoreCase("Baker")));
        assertTrue(results.stream().anyMatch(c -> c.getLastName().equalsIgnoreCase("Brown")));
    }

    @Test
    void testFindByLastNameRange() {
        // Verifies range extraction from 'Anderson' through 'Brown'
        List<Contact> results = repository.findByLastNameRange("Anderson", "Brown");
        assertEquals(3, results.size());

        assertEquals("Anderson", results.get(0).getLastName());
        assertEquals("Baker", results.get(1).getLastName());
        assertEquals("Brown", results.get(2).getLastName());
    }
}