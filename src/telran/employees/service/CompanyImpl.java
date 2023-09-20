package telran.employees.service;

import java.util.*;

import telran.employees.dto.Employee;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<>(); //most effective structure for the interface methods
	@Override
	public boolean addEmployee(Employee empl) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Employee removeEmployee(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee getEmployee(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

}
