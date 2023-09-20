package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.Employee;
import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTest {
	Company company;
	Employee empl1;
	Employee empl2;
	Employee empl3;
	Employee empl4;
	Employee empl5;
	List<Employee> employees = new ArrayList<Employee>();;
	
	final static String TEST_FILE_NAME = "test.data";
	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		empl1 = new Employee(123l, "Vasya", "manager", 12500, LocalDate.of(1987, 7, 3));
		empl2 = new Employee(124l, "Ron", "developer", 15300, LocalDate.of(1989, 1, 10));
		empl3 = new Employee(125l, "Glory", "manager", 11500, LocalDate.of(1997, 5, 1));
		empl4 = new Employee(126l, "Arnon", "developer", 17000, LocalDate.of(1999, 3, 17));
		empl5 = new Employee(138l, "Jorge", "engineer", 18340, LocalDate.of(1982, 12, 23));
		company.addEmployee(empl2);
		company.addEmployee(empl4);
		
		
	}

	@Test
	void testAddEmployee() {
		company.addEmployee(empl5);
		assertTrue(empl5.equals(company.getEmployee(empl5.id())));
		assertTrue(company.addEmployee(empl3));
		company.addEmployee(empl1);
		assertFalse(company.addEmployee(empl1));
	}

	@Test
	void testRemoveEmployee() {
		company.addEmployee(empl1);
		assertEquals(empl1, company.removeEmployee(empl1.id()));
		assertEquals(empl2, company.removeEmployee(empl2.id()));
		assertEquals(null, company.removeEmployee(empl3.id()));
		
	}

	@Test
	void testGetEmployee() {
		assertEquals(null, company.getEmployee(empl5.id()));
		assertEquals(empl2, company.getEmployee(empl2.id()));
		assertEquals(null, company.getEmployee(empl1.id()));
		company.addEmployee(empl1);
		assertEquals(empl1, company.getEmployee(empl1.id()));
	}

	@Test
	void testGetEmployees() {
		employees = company.getEmployees();
		int size = company.getEmployees().size();
		assertEquals(size, employees.size());
		int index = employees.indexOf(empl2);
		assertEquals(employees.get(index), company.getEmployee(empl2.id()));
	}
	
	@Test
	@Order(2)
	void testRestore() throws Exception {
		long id = empl1.id();
		company.restore(TEST_FILE_NAME);
		assertEquals(empl1, company.getEmployee(id));
	}
	@Test
	@Order(1)
	void testSave() throws Exception {
		company.addEmployee(empl1);
		company.save(TEST_FILE_NAME);
	}

}
