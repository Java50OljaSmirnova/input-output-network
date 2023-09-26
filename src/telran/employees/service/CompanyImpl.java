package telran.employees.service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import telran.employees.dto.*;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<Long, Employee>();
	TreeMap<LocalDate, List<Employee>> employeesDate = new TreeMap<>();
	HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>();
	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = employees.putIfAbsent(empl.id(), empl) == null;
		if(res) {
			LocalDate date = empl.birthDate();
			Integer salary = empl.salary();
			String department = empl.department();
			addToIndex(empl, date, employeesDate);
			addToIndex(empl, salary, employeesSalary);
			addToIndex(empl, department, employeesDepartment);
		}
		return res;
	}

	private <T> void addToIndex(Employee empl, T key, Map<T, List<Employee>> map) {
		map.computeIfAbsent(key, k -> new LinkedList<>()).add(empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl = employees.remove(id);
		if(empl != null) {
			LocalDate date = empl.birthDate();
			Integer salary = empl.salary();
			String department = empl.department();
			removeFromIndex(empl, date, employeesDate);
			removeFromIndex(empl, salary, employeesSalary);
			removeFromIndex(empl, department, employeesDepartment);
		}
		return empl;
	}

	private <T> void removeFromIndex(Employee empl, T key, Map<T, List<Employee>> map) {

		List<Employee> employeesCol = map.get(key);
		employeesCol.remove(empl);
		if (employeesCol.isEmpty()) {
			map.remove(key);
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
		
		return new LinkedList<>(employees.values().stream()
				.collect(Collectors.groupingBy(Employee::department, Collectors.averagingInt(Employee::salary)))
				.entrySet().stream().map(e -> new DepartmentSalary(e.getKey(), e.getValue())).toList());
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
		
		Collection<Employee> employeesCol = employeesDepartment.get(department);
		ArrayList<Employee> res = new ArrayList<>();
		if (employeesCol != null) {
			res.addAll(employeesCol);
		}
		return res;
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
	
		return employeesSalary.subMap(salaryFrom, salaryTo).values().stream().flatMap(List::stream).toList();
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		
		LocalDate dateFrom = getDate(ageTo);
		LocalDate dateTo = getDate(ageFrom);
		return employeesDate.subMap(dateFrom, dateTo).values().stream().flatMap(List::stream).toList();
	}

	private LocalDate getDate(int age) {
		LocalDate currentDate = LocalDate.now();

		return currentDate.minusYears(age);
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
		Employee empl = removeEmployee(id);
		if(empl != null) {
			Employee emplUpdate = new Employee(id, empl.name(), department, empl.salary(), empl.birthDate());
			addEmployee(emplUpdate);
				
		}

		return empl;
	}

}
