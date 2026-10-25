package main;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    void save(Contact contact);
    Optional<Contact> findById(String id);
    List<Contact> findAll();
    boolean deleteById(String id);
    boolean existsById(String id);
}
