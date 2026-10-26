package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryContactRepository implements ContactRepository {
    // Main index where O(1) average lookup, insert, delete by contactID
    private final Map<String, Contact> idIndex = new ConcurrentHashMap<>();

    // Secondary sorted index where O(log n) prefix and range scans by lastName
    private final ConcurrentSkipListMap<String, List<Contact>> lastNameIndex = new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER);

    @Override 
    public synchronized void save(Contact contact) {
        Contact existing = idIndex.get(contact.getContactID());

        // If update existing contact and last name changed, clean old tree index entry
        if (existing != null && !existing.getLastName().equalsIgnoreCase(contact.getLastName())) {
            removeFromLastNameIndex(existing);
        }

        // Puts into main index, O(1)
        idIndex.put(contact.getContactID(), contact);

        // Maintains secondary sorted tree index, O(log n)
        lastNameIndex.computeIfAbsent(contact.getLastName(), k -> new CopyOnWriteArrayList<>()).add(contact);
    }

    @Override
    public Optional<Contact> findById(String id) {
        if (id == null) return Optional.empty();
        // O(1) constant time retrieval
        return Optional.ofNullable(idIndex.get(id));
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(idIndex.values());
    }
    
    @Override
    public synchronized boolean deleteById(String id) {
        if (id == null) return false;
        Contact removed = idIndex.remove(id); // O(1)
        if (removed != null) {
            removeFromLastNameIndex(removed);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean existsById(String id) {
        if (id == null) return false;
        return idIndex.containsKey(id); // O(1)
    }

    @Override
    public List<Contact> findByLastNamePrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Collections.emptyList();
        }
        List<Contact> results = new ArrayList<>();
        // O(log n) sub map boundary lookup
        String toKey = prefix + Character.MAX_VALUE;
        for (List<Contact> list : lastNameIndex.subMap(prefix, true, toKey, true).values()) {
            results.addAll(list);
        }
        return results;
    }

    @Override
    public List<Contact> findByLastNameRange(String startLastName, String endLastName) {
        if (startLastName == null || endLastName == null) {
            return Collections.emptyList();
        }
        List<Contact> results = new ArrayList<>();
        // O(log n + m) bounded range query
        for (List<Contact> list : lastNameIndex.subMap(startLastName, true, endLastName, true).values()) {
            results.addAll(list);
        }
        return results;
    }

    private void removeFromLastNameIndex(Contact contact) {
        List<Contact> list = lastNameIndex.get(contact.getLastName());
        if(list != null) {
            list.remove(contact);
            if(list.isEmpty()) {
                lastNameIndex.remove(contact.getLastName());
            }
        }
    }
}