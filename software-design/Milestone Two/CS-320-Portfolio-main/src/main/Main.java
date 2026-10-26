package main;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) {
        System.out.println("=== Contact Service Demonstration ===");

        
        ContactService service = new ContactService();

        
        Contact contact1 = new Contact("C1001", "Jane", "Doe", "123 Main St, Anytown", "5551234567");
        Contact contact2 = new Contact("C1002", "John", "Smith", "456 Oak Ave, Metropolis", "5559876543");

        
        System.out.println("\nAdding contacts...");
        service.addContact(contact1);
        service.addContact(contact2);
        System.out.println("Total contacts registered: " + service.getContacts().size());

        
        System.out.println("\nRetrieving contact C1001:");
        Contact retrieved = service.getContact("C1001");
        if (retrieved != null) {
            System.out.println("Found: " + retrieved.getFirstName() + " " + retrieved.getLastName() 
                               + " | Phone: " + retrieved.getPhoneNum() 
                               + " | Address: " + retrieved.getAddress());
        }

        
        System.out.println("\nUpdating address for C1001...");
        boolean updateSuccess = service.updateAddress("C1001", "789 Pine Rd, New City");
        System.out.println("Update status: " + updateSuccess);
        System.out.println("New Address: " + service.getContact("C1001").getAddress());

        
        System.out.println("\nDeleting contact C1002...");
        boolean deleteSuccess = service.deleteContact("C1002");
        System.out.println("Delete status: " + deleteSuccess);
        System.out.println("Remaining contacts: " + service.getContacts().size());

        
        System.out.println("\nTesting input validation failure (First name > 10 chars):");
        try {
            new Contact("C1003", "ChristopherLongName", "Lee", "100 Elm St", "5550001111");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected validation exception: " + e.getMessage());
        }

        System.out.println("\n=== End of Demonstration ===");
    }
}