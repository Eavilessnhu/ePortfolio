package main;

import java.util.List;

public class ContactService {
    private final ContactRepository repository;

    // Primary constructor using Dependency Injection
    public ContactService(ContactRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }

    // Default constructor now defaults to the persistent MongoDB repository
    public ContactService() {
        this(new MongoContactRepository());
    }

    public List<Contact> getContacts() {
        return repository.findAll();
    }

    public Contact getContact(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found for ID: " + id));
    }

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (repository.existsById(contact.getContactID())) {
            throw new DuplicateContactException("Contact already exists with ID: " + contact.getContactID());
        }
        repository.save(contact);
    }

    public boolean deleteContact(String id) {
        if (!repository.existsById(id)) {
            throw new ContactNotFoundException("Cannot delete: contact not found for ID: " + id);
        }
        return repository.deleteById(id);
    }

    public boolean updateFirstName(String id, String newFirstName) {
        Contact contact = getContact(id);
        contact.setFirstName(newFirstName);
        repository.save(contact);
        return true;
    }

    public boolean updateLastName(String id, String newLastName) {
        Contact contact = getContact(id);
        contact.setLastName(newLastName);
        repository.save(contact);
        return true;
    }

    public boolean updateNumber(String id, String newNumber) {
        Contact contact = getContact(id);
        contact.setPhoneNum(newNumber);
        repository.save(contact);
        return true;
    }

    public boolean updateAddress(String id, String newAddress) {
        Contact contact = getContact(id);
        contact.setAddress(newAddress);
        repository.save(contact);
        return true;
    }

    public List<Contact> searchByLastNamePrefix(String prefix) {
        return repository.findByLastNamePrefix(prefix);
    }

    public List<Contact> getContactsInLastNameRange(String start, String end) {
        return repository.findByLastNameRange(start, end);
    }
}