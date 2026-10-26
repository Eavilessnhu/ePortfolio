package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Contact;

class ContactTest {
	
	private static Contact contact;
	
	@BeforeEach
	void resetContact() {
		contact = new Contact();
	}

	@Test
	void testContactID() {
		Contact contactID = new Contact("0987654321","Elias","Aviles","941 Maple Ave","7177159602");
		Assertions.assertTrue(contactID.getContactID().equals("0987654321"));
		Assertions.assertTrue(contactID.getAddress().equals("941 Maple Ave"));
		Assertions.assertTrue(contactID.getFirstName().equals("Elias"));
		
		
		
		assertThrows(IllegalArgumentException.class, () -> {
		new Contact("09876543231","Elias","Aviles","myAddress","23233133133");
		}); 
	}
	
	@Test
	void testSetPhoneNum() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			contact.setPhoneNum("48579802470359723908");
		});
	}
	
	@Test
	void testSetAddress() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			contact.setAddress("4ur9q43895fakl;asdfafeaasdfefakldsfiuajeoirfu9843qyrijalelfhoiuiawqgref9jao;ifelioashdf");
		});
	}
	
	@Test
	void testSetFirstName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			contact.setFirstName("afadlfoiaejoi;");
		});
	}
	
	@Test
	void testSetLastName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			contact.setLastName("afopoiuaepfu9pae9fu923");
		});
	}
}

