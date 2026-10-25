package main;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    void save(Contact contact);
    Optional<Contact> findById(String id);
    List<Contact> findAll();
    boolean deleteById(String id);
    boolean existsById(String id);

    // This is supposed to provide algorithmic additions for O(log n) tree traversals
    List<Contact> findByLastNamePrefix(String prefix);
    List<Contact> findByLastNameRange(String startLastName, String endLastName);
}
