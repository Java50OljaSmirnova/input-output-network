package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTests {
//TODO
	final static String TEST_FILE_NAME = "test.data";
	@BeforeEach
	void setUp() throws Exception {
		//TODO
	}

	@Test
	void testAddEmployee() {
		//TODO
	}

	@Test
	void testRemoveEmployee() {
		//TODO
	}

	@Test
	void testGetEmployee() {
		//TODO
	}

	@Test
	void testGetEmployees() {
		//TODO
	}
	@Test
	@Order(2)
	void testRestore() {
		//TODO
	}
	@Test
	@Order(1)
	void testSave() {
		//TODO
	}

}