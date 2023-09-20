package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ObjectStreamTest {
	
	static final String FILE_NAME = "person.data";

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@Order(1)
	void writePersonTest() throws Exception{
		Person person = new Person(123l, "Vasya");
		person.person = person;
		assertEquals(person.id, person.person.person.person.person.id);
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
			output.writeObject(person);
		}
	}
	@Test
	@Order(2)
	void readPersonTest() throws Exception{
		Person person = null;
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_NAME))){
			person = (Person) input.readObject();
		}
		assertEquals(123l, person.person.person.person.person.id);
	}

}
class Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	long id;
	String name;
	Person person;
	public Person(long id, String name) {
		this.id = id;
		this.name = name;
	}

}