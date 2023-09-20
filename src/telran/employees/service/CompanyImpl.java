package telran.employees.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import telran.employees.dto.Employee;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<Long, Employee>();
	List<Employee> employeesComp = new ArrayList<Employee>();
	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		if(!empl.equals(employees.get(empl.id()))){
			employees.put(empl.id(), empl);
			res = true;
		}
		return res;
	}

	@Override
	public Employee removeEmployee(long id) {
		
		return employees.remove(id);
	}

	@Override
	public Employee getEmployee(long id) {
		
		return employees.get(id);
	}

	@Override
	public List<Employee> getEmployees() {
		
		employeesComp.addAll(employees.values());
		return employeesComp;
	}

}
