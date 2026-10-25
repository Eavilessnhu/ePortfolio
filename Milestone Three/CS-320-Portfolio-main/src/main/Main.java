package main;

import java.util.List;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) {
        // Initialize service using default in-memory multi-index repository
        ContactService service = new ContactService();

        // 1. Seed initial records
        service.addContact(new Contact("C101", "Alice", "Anderson", "100 Pine St", "5551112222"));
        service.addContact(new Contact("C102", "Bob", "Brown", "200 Oak St", "5553334444"));
        service.addContact(new Contact("C103", "Charlie", "Baker", "300 Elm St", "5555556666"));

        System.out.println("Active contacts registered: " + service.getContacts().size());

        // 2. Quick operational verification
        Contact primary = service.getContact("C101");
        System.out.println("Verified record: " + primary.getFirstName() + " " + primary.getLastName());

        // 3. Algorithmic index inspection
        List<Contact> prefixMatches = service.searchByLastNamePrefix("B");
        System.out.println("Indexed search ('B' prefix): " + prefixMatches.size() + " records found");
    }
}