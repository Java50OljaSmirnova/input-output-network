package telran.employees.net;

import java.util.List;

import telran.employees.CompanyApi;
import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.dto.UpdateDepartmentData;
import telran.employees.dto.UpdateSalaryData;
import telran.employees.service.Company;
import telran.net.*;

public class CompanyNetworkProxy implements Company{
	private NetworkHandler networkHandler;

	public CompanyNetworkProxy(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	@Override
	public boolean addEmployee(Employee empl) {
	
		return networkHandler.send(CompanyApi.EMPLOYEE_ADD, empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		
		return networkHandler.send(CompanyApi.EMPLOYEE_REMOVE, id);
	}

	@Override
	public Employee getEmployee(long id) {
		
		return networkHandler.send(CompanyApi.EMPLOYEE_GET, id);
	}

	@Override
	public List<Employee> getEmployees() {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_ALL, null);
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_DEPARTMENT_SALARY_DISTRIBUTION, null);
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_SALARY_DISTRUBUTION, interval);
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_BY_DEPARTMENT, department);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_BY_SALARY, new int[] {salaryFrom, salaryTo});
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		
		return networkHandler.send(CompanyApi.EMPLOYEES_BY_AGE, new int[] {ageFrom, ageTo});
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		
		return networkHandler.send(CompanyApi.EMPLOYEE_SALARY_UPDATE, new UpdateSalaryData(id, newSalary));
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		
		return networkHandler.send(CompanyApi.EMPLOYEE_DEPARTMENT_UPDATE, new UpdateDepartmentData(id, department));
	}


}
