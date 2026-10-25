package test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Contact;
import main.ContactService;

class ContactServiceTest {
	private static ContactService contactService;
	
	@BeforeEach
	void setUpContactService() {
		contactService = new ContactService();
		contactService.addContact(new Contact("1234567","Daniel","Ortiz","101 North Pointe Blvd","7172939293"));
	}

    @Test
    void updateFirstName(){
        Contact c = new Contact("1234567","Daniel","Ortiz","101 North Pointe Blvd","7172939293");
        ContactService cs = new ContactService();
        cs.addContact(new Contact("1234567","Daniel","Aviles","7172939293","7172939293"));
        cs.updateLastName("1234567","Ortiz");
        
        // Changed Assert.assertEquals to Assertions.assertEquals:
        Assertions.assertEquals(c.getLastName(), cs.getContact("1234567").getLastName());
    }
    
    @Test
    void addContactFailsTest() {
    	contactService.addContact(new Contact("1234567","Test","Ortiz","101 North Pointe Blvd","7172939293"));
    	Assertions.assertFalse(contactService.getContact("1234567").getFirstName().equals("Test"));
    }
    
    @Test
    void deleteContactSucceedsTest() {
    	Assertions.assertTrue(contactService.deleteContact("1234567"));
    }
    
    @Test
    void updateAddress() {
    	contactService.updateAddress("1234567", "new Address");
    	Assertions.assertEquals(contactService.getContact("1234567").getAddress(), "new Address");
    }
}