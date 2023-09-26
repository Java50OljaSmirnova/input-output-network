package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTest {
	private static final long ID1 = 123;
	private static final String DEP1 = "dep1";
	private static final int SALARY1 = 10000;
	private static final int YEAR1 = 2000;
	private static final LocalDate DATE1 = LocalDate.ofYearDay(YEAR1, 100);
	private static final long ID2 = 124;
	private static final long ID3 = 125;
	private static final long ID4 = 126;
	private static final long ID5 = 127;
	private static final String DEP2 = "dep2";
	private static final String DEP3 = "dep3";
	private static final int SALARY2 = 5000;
	private static final int SALARY3 = 15000;
	private static final int YEAR2 = 1990;
	private static final LocalDate DATE2 = LocalDate.ofYearDay(YEAR2, 100);
	private static final int YEAR3 = 2003;
	private static final LocalDate DATE3 = LocalDate.ofYearDay(YEAR3, 100);
	private static final long ID_NOT_EXIST = 10000000;
	
	Employee empl1 = new Employee(ID1, "name", DEP1, SALARY1, DATE1);
	Employee empl2 = new Employee(ID2, "name", DEP2, SALARY2, DATE2);
	Employee empl3 = new Employee(ID3, "name", DEP1, SALARY1, DATE1);
	Employee empl4 = new Employee(ID4, "name", DEP2, SALARY2, DATE2);
	Employee empl5 = new Employee(ID5, "name", DEP3, SALARY3, DATE3);
	Employee[] employees = {empl1, empl2, empl3, empl4, empl5};
	Company company;
	
	final static String TEST_FILE_NAME = "test.data";
	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		for(Employee empl: employees) {
			company.addEmployee(empl);
		}
		
		
	}

	@Test
	void testAddEmployee() {
		assertFalse(company.addEmployee(empl1));
		assertTrue(company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, SALARY1, DATE1)));
	}

	@Test
	void testRemoveEmployee() {
		assertNull(company.removeEmployee(ID_NOT_EXIST));
		assertEquals(empl1, company.removeEmployee(ID1));
		Employee[] expected = {empl2, empl3, empl4, empl5};
		Employee[]actual = company.getEmployees()
				.toArray(Employee[]::new);
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, actual);
	}
		

	@Test
	void testGetEmployee() {
		assertEquals(empl1, company.getEmployee(ID1));
		assertNull(company.getEmployee(ID_NOT_EXIST));
	}

	@Test
	void testGetEmployees() {
		runTest(employees, company);
		
	}
	
	@Test
	@Order(2)
	void testRestore() throws Exception {
		
		Company newCompany = new CompanyImpl();
		newCompany.restore(TEST_FILE_NAME);
		runTest(employees, newCompany);

	}

	private void runTest(Employee[] expected, Company company) {
		Employee[]actual = company.getEmployees()
				.toArray(Employee[]::new);
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));

		assertArrayEquals(expected, actual);
	}
	@Test
	@Order(1)
	void testSave() throws Exception {
		
		company.save(TEST_FILE_NAME);
	}
	
	//Test of CW/HW #34
	@Test
	void testGetDepartmentSalaryDistribution() {
		DepartmentSalary [] expected = {
				new DepartmentSalary(DEP2, SALARY2),
				new DepartmentSalary(DEP1, SALARY1),
				new DepartmentSalary(DEP3, SALARY3)
			};
			DepartmentSalary [] actual = company.getDepartmentSalaryDistribution()
					.stream().sorted((ds1, ds2) -> Double.compare(ds1.salary(), ds2.salary())).
					toArray(DepartmentSalary[]::new);
			assertArrayEquals(expected, actual);
	};
	@Test
	void testGetSalaryDistribution() {
		company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, 9999, DATE1));
		SalaryDistribution sd1 = new SalaryDistribution(5_000, 10_000, 3);
		SalaryDistribution sd2 = new SalaryDistribution(10_000, 15_000, 2);
		SalaryDistribution sd3 = new SalaryDistribution(15_000, 20_000, 1);
		List<SalaryDistribution> expected = List.of(sd1, sd2, sd3);
		List<SalaryDistribution> actual = company.getSalaryDistribution(5000);
		assertIterableEquals(expected, actual);
	};
	@Test
	void testGetEmployeesByDepartment() {
		runGetByDepartmentTest("XXX", new Employee[0]);
		runGetByDepartmentTest(DEP1, new Employee[] {empl1, empl3});
	};
	
	private void runGetByDepartmentTest(String department, Employee[] expected) {
		List<Employee> employees = company.getEmployeesByDepartment(department);
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));

	}
	private void runGetBySalaryTest(int salaryFrom, int salaryTo,
			Employee[]expected) {
		List<Employee> employees = new LinkedList<>(company.getEmployeesBySalary(salaryFrom, salaryTo));
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));
	}
	private void runGetByAgeTest(int ageFrom, int ageTo,
			Employee[]expected) {
		List<Employee> employees = new LinkedList<>(company.getEmployeesByAge(ageFrom, ageTo));
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));
	}

	@Test
	void testGetEmployeesBySalary() {
		runGetBySalaryTest(SALARY2, SALARY3 + 1, employees);
		runGetBySalaryTest(SALARY3 + 1, 100000000, new Employee[0]);
		runGetBySalaryTest(SALARY2, SALARY1+1, new Employee[] {empl1, empl2, empl3, empl4});
			
	};
	@Test
	void testGetEmployeesByAge() {
		runGetByAgeTest(getAge(DATE1), getAge(DATE2) + 1, new Employee[] {empl1, empl2, empl3, empl4});
		runGetByAgeTest(75, 80, new Employee[] {});
		
	};
	@Test
	void testUpdateSalary() {
		company.updateSalary(ID5, SALARY1);
		runGetBySalaryTest(SALARY3, SALARY3 + 1, new Employee[0]);
		runGetBySalaryTest(SALARY2, SALARY1 + 1,employees);
		
	};
	@Test
	void testUpdateDepartment() {
		company.updateDepartment(ID5, DEP1);
		runGetByDepartmentTest(DEP1, new Employee[] {empl1, empl3, empl5});
		runGetByDepartmentTest(DEP3, new Employee[0]);
	};
	
	private int getAge(LocalDate birthDate) {

		return (int)ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}

	
}
