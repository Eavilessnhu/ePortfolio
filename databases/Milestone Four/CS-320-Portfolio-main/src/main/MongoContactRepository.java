package main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MongoContactRepository implements ContactRepository {
    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    // Primary constructor accepting custom connection settings (used by unit tests)
    public MongoContactRepository(String connectionUri, String dbName, String collectionName) {
        this.mongoClient = MongoClients.create(connectionUri);
        MongoDatabase database = this.mongoClient.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
        initIndexes();
    }

    // Default constructor configured for your MongoDB Atlas cluster
    public MongoContactRepository() {
        this(
            "mongodb+srv://eliavi649_db_user:M08Ct8PIun2KvwIo@cluster0.1rzafmp.mongodb.net",
            "cs499_db",
            "contacts"
        );
    }

    private void initIndexes() {
        // Enforces unique constraint matching the business rule in ContactService
        collection.createIndex(
            Indexes.ascending("contactId"),
            new IndexOptions().unique(true)
        );

        // B-tree index on lastName to support O(log n) range queries and prefix searches
        collection.createIndex(Indexes.ascending("lastName"));
    }

    @Override
    public synchronized void save(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        Document doc = new Document("contactId", contact.getContactID())
                .append("firstName", contact.getFirstName())
                .append("lastName", contact.getLastName())
                .append("address", contact.getAddress())
                .append("phoneNum", contact.getPhoneNum());

        // Upsert: updates existing document if contactId matches, inserts if not
        collection.replaceOne(
            Filters.eq("contactId", contact.getContactID()),
            doc,
            new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public Optional<Contact> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }

        Document doc = collection.find(Filters.eq("contactId", id)).first();
        return doc != null ? Optional.of(mapDocumentToContact(doc)) : Optional.empty();
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        for (Document doc : collection.find()) {
            contacts.add(mapDocumentToContact(doc));
        }
        return contacts;
    }

    @Override
    public synchronized boolean deleteById(String id) {
        if (id == null) {
            return false;
        }

        return collection.deleteOne(Filters.eq("contactId", id)).getDeletedCount() > 0;
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) {
            return false;
        }

        return collection.countDocuments(Filters.eq("contactId", id)) > 0;
    }

    @Override
    public List<Contact> findByLastNamePrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Collections.emptyList();
        }

        // B-tree prefix query: ^<prefix>, case-insensitive
        Pattern regexPattern = Pattern.compile("^" + Pattern.quote(prefix), Pattern.CASE_INSENSITIVE);
        Bson filter = Filters.regex("lastName", regexPattern);

        List<Contact> results = new ArrayList<>();
        for (Document doc : collection.find(filter).sort(Indexes.ascending("lastName"))) {
            results.add(mapDocumentToContact(doc));
        }
        return results;
    }

    @Override
    public List<Contact> findByLastNameRange(String startLastName, String endLastName) {
        if (startLastName == null || endLastName == null) {
            return Collections.emptyList();
        }

        // Bounded range query using $gte and $lte operators
        Bson filter = Filters.and(
            Filters.gte("lastName", startLastName),
            Filters.lte("lastName", endLastName)
        );

        List<Contact> results = new ArrayList<>();
        for (Document doc : collection.find(filter).sort(Indexes.ascending("lastName"))) {
            results.add(mapDocumentToContact(doc));
        }
        return results;
    }

    private Contact mapDocumentToContact(Document doc) {
        return new Contact(
            doc.getString("contactId"),
            doc.getString("firstName"),
            doc.getString("lastName"),
            doc.getString("address"),
            doc.getString("phoneNum")
        );
    }
}