package telran.employees.controller;

import java.nio.channels.AlreadyBoundException;
import java.time.LocalDate;
import java.util.*;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.view.*;

public class CompanyController {
	private static final long MIN_ID = 100000;
	private static final long MAX_ID = 999999;
	private static final String[] DEPARTMENTS = {"QA", "Development", "Audit", "Accounting", "Management"};
	private static final int MIN_SALARY = 7000;
	private static final int MAX_SALARY = 50000;
	private static final int MIN_YEAR = 1950;
	private static final int MAX_YEAR = 2001;
	private static Company company;
	public static ArrayList<Item> getItems(Company company) {
		CompanyController.company = company;
		List<Item> itemsList = getItemsList();
		ArrayList<Item> res = new ArrayList<>(itemsList);
		return res;
	}
	private static List<Item> getItemsList() {
		
		return List.of(
				Item.of("Hire new Employee", CompanyController::addEmployee),
				Item.of("Fire new Employee", CompanyController::removeEmployee),
				Item.of("Display data of Employee", CompanyController::getEmployee),
				Item.of("Display data of all Employees", CompanyController::getEmployees),
				Item.of("Distribution of salary by departments", CompanyController::getDepartmentSalaryDistribution),
				Item.of("Salary distribution per interval", CompanyController::getSalaryDistribution),
				Item.of("Display data of Employees in department", CompanyController::getEmployeesByDepartment),
				Item.of("Display data of Employees by salary", CompanyController::getEmployeesBySalary),
				Item.of("Display data of Employees by age", CompanyController::getEmployeesByAge),
				Item.of("Update salary", CompanyController::updateSalary),
				Item.of("Update department", CompanyController::updateDepartment)
				);
	}
	static void addEmployee(InputOutput io) {
		long id = io.readLong("Enter employee identity", "Wrong identity", MIN_ID, MAX_ID);
		if(company.getEmployee(id) != null) {
			throw new RuntimeException("Employee with id " + id + "already exist");
		}
		String name = io.readString("Enter name", "Wrong name", str -> str.matches("[A-Z][a-z]{2,}"));
		String department = io.readString("Enter department", "Wrong department " + Arrays.deepToString(DEPARTMENTS), new HashSet<String>(List.of(DEPARTMENTS)));
		int salary = io.readInt("Enter salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		LocalDate birthDate = io.readIsoDate("Enter birthdate in ISO format", "Wrong birthdate", 
				LocalDate.of(MIN_YEAR, 1, 1), LocalDate.of(MAX_YEAR, 12, 31));
		Employee empl = new Employee(id, name, department, salary, birthDate);
		boolean res = company.addEmployee(empl );
		io.writeLine(res ? "Employee has been added" : "Employee already exist");
	}
	static void removeEmployee(InputOutput io) {
		long id = io.readLong("Enter employee identity", "Wrong identity", MIN_ID, MAX_ID);
		if(company.getEmployee(id) == null) {
			throw new RuntimeException("Employee with id " + id + "doesn't exist");
		}
		Employee empl = company.removeEmployee(id);
		io.writeLine("Employee " + empl + " removed");
	}
	static void getEmployee(InputOutput io) {
		long id = io.readLong("Enter employee identity", "Wrong identity", MIN_ID, MAX_ID);
		Employee empl = company.getEmployee(id);
		io.writeLine(empl == null ? "Employee with id " + id + "doesn't exist" : "" + empl);
	}
	static void getEmployees(InputOutput io) {
		List<Employee> employees = company.getEmployees();
		employees.forEach(System.out :: println);
	}
	static void getDepartmentSalaryDistribution(InputOutput io) {
		List<DepartmentSalary> departments = company.getDepartmentSalaryDistribution().
				stream().sorted((ds1, ds2) -> Double.compare(ds1.salary(), ds2.salary())).toList();
		departments.forEach(System.out :: println);
	}
	static void getSalaryDistribution(InputOutput io) {
		int interval = io.readInt("Enter salary interval", "Wrong inteval");
		List<SalaryDistribution> employees = company.getSalaryDistribution(interval);
		employees.forEach(System.out::println);
	}
	static void getEmployeesByDepartment(InputOutput io) {
		String department = io.readString("Enter department", "Wrong department " + Arrays.deepToString(DEPARTMENTS),
				new HashSet<String>(List.of(DEPARTMENTS)));
		List<Employee> employees = company.getEmployeesByDepartment(department);
		employees.forEach(System.out::println);
	}
	static void getEmployeesBySalary(InputOutput io) {
		int salaryFrom = io.readInt("Enter first number for inteval, salary from", "Wrong salary");
		int salaryTo = io.readInt("Enter second number for inteval, salary to", "Wrong salary");
		List<Employee> employees = company.getEmployeesBySalary(salaryFrom, salaryTo);
		employees.forEach(System.out::println);
	}
	static void getEmployeesByAge(InputOutput io) {
		int ageFrom = io.readInt("Enter first number for inteval, age from", "Wrong age");
		int ageTo = io.readInt("Enter second number for inteval, age to", "Wrong age");
		List<Employee> employees = company.getEmployeesByAge(ageFrom, ageTo);
		employees.forEach(System.out::println);
	}
	static void updateSalary(InputOutput io) {
		long id = io.readLong("Enter identity", "Wrong identity", MIN_ID, MAX_ID);
		if(company.getEmployee(id) == null) {
			throw new RuntimeException("Employee with ID " + id + " doesn't exist");
		}
		int salary = io.readInt("Enter new salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		company.updateSalary(id, salary);
		io.writeLine("Salary at employee by ID" + id + "was update");
	}
	static void updateDepartment(InputOutput io) {
		long id = io.readLong("Enter identity", "Wrong identity", MIN_ID, MAX_ID);
		if(company.getEmployee(id) == null) {
			throw new RuntimeException("Employee with ID " + id + " doesn't exist");
		}
		String department = io.readString("Enter new department", "Wrong depertment" + Arrays.deepToString(DEPARTMENTS), 
				new HashSet<String>(List.of(DEPARTMENTS)));
		company.updateDepartment(id, department);
		io.writeLine("Department of employee by ID" + id + "was update");
	}
	



}
