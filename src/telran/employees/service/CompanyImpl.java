package telran.employees.service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import telran.employees.dto.*;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<Long, Employee>();
	TreeMap<Integer, List<Employee>> employeesAge = new TreeMap<>();
	HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>();
	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = employees.putIfAbsent(empl.id(), empl) == null;
		if(res) {
			addToIndexMap(empl, empl.department(), empl.salary(), getAge(empl.birthDate()));
		}
		return res;
	}

	private void addToIndexMap(Employee empl, String department, int salary, int age) {
		
		employeesDepartment.computeIfAbsent(department, k -> new LinkedList<>()).add(empl);
		employeesAge.computeIfAbsent(age , k -> new LinkedList<>()).add(empl);
		employeesSalary.computeIfAbsent(salary , k -> new LinkedList<>()).add(empl);
		
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl = employees.remove(id);
		if(empl != null) {
			removeFromIndexMap(empl, empl.department(), empl.salary(), getAge(empl.birthDate()));
		}
		return empl;
	}

	private void removeFromIndexMap(Employee empl, String department, int salary, int age) {
		List<Employee> listDepartment = employeesDepartment.get(department);
		List<Employee> listAge = employeesAge.get(age);
		List<Employee> listSalary = employeesSalary.get(salary);
		listDepartment.remove(empl);
		listAge.remove(empl);
		listSalary.remove(empl);
		if(listDepartment.isEmpty()) {
			employeesDepartment.remove(department);
		}
		if(listAge.isEmpty()) {
			employeesAge.remove(age);
		}
		if(listSalary.isEmpty()) {
			employeesSalary.remove(salary);
		}
		
	}

	@Override
	public Employee getEmployee(long id) {
		
		return employees.get(id);
	}

	@Override
	public List<Employee> getEmployees() {
		
		return new ArrayList<>(employees.values());
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		
		return employeesDepartment.entrySet().stream()
				.map(d -> new DepartmentSalary(d.getKey(), 
					d.getValue().stream().mapToInt(e -> e.salary()).sum() / d.getValue().size())).toList();
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		Map <Integer, Long> mapIntervalNumbers = employees.values().stream()
				.collect(Collectors.groupingBy(e -> e.salary() / interval, Collectors.counting()));
		return mapIntervalNumbers.entrySet().stream()
				.map(e -> new SalaryDistribution(e.getKey() * interval, e.getKey() * interval + interval, e.getValue().intValue()))
				.sorted((sd1, sd2) -> Integer.compare(sd1.min(), sd2.min())).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		
		return employeesDepartment.get(department);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
	
		return employeesSalary.subMap(salaryFrom, salaryTo).values().stream().flatMap(List::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		
		return employeesAge.subMap(ageFrom, ageTo).values().stream().flatMap(List::stream).toList();
	}

	private int getAge(LocalDate birthDate) {
		
		return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		Employee empl = removeEmployee(id);
		if(empl != null) {
			Employee emplUpdate = new Employee(id, empl.name(), empl.department(), newSalary, empl.birthDate());
			addEmployee(emplUpdate);
				
		}

		return empl;
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		Employee empl = employees.get(id);
		if(empl != null) {
			int salary = empl.salary();
			int age = getAge(empl.birthDate());
			removeFromIndexMap(empl, empl.department(), salary, age);
			employees.get(id).department().replaceAll(empl.department(), department);
			Employee emplUpdateField = employees.get(id);
			addToIndexMap(emplUpdateField, department, salary, age);
		}

		return empl;
	}

}
