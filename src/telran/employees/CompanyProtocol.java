package telran.employees;

import java.io.Serializable;
import java.util.ArrayList;

import telran.employees.dto.*;
import telran.employees.service.*;
import telran.net.ApplProtocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements ApplProtocol {
	private Company company;

	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		Serializable requestData = request.requestData();
		String requestType = request.requestType();
		Response response = null;
		Serializable responseData = 0;
		Integer defaultValue = Integer.MAX_VALUE;
		try {
			responseData = switch(requestType) {
			case CompanyApi.EMPLOYEE_ADD -> employee_add(requestData);
			case CompanyApi.EMPLOYEE_REMOVE -> employee_remove(requestData);
			case CompanyApi.EMPLOYEE_GET -> employees_get(requestData);
			case CompanyApi.EMPLOYEES_ALL -> employees_all(requestData);
			case CompanyApi.EMPLOYEES_DEPARTMENT_SALARY_DISTRIBUTION -> 
			        employees_department_salary_distribution(requestData);
			case CompanyApi.EMPLOYEES_SALARY_DISTRUBUTION -> employees_salary_distribution(requestData);
			case CompanyApi.EMPLOYEES_BY_DEPARTMENT -> employees_byDepartment(requestData);
			case CompanyApi.EMPLOYEES_BY_SALARY -> employees_bySalary(requestData);
			case CompanyApi.EMPLOYEES_BY_AGE -> employees_byAge(requestData);
			case CompanyApi.EMPLOYEE_SALARY_UPDATE-> employee_salary_update(requestData);
			case CompanyApi.EMPLOYEE_DEPARTMENT_UPDATE -> employee_department_update(requestData);
			default -> defaultValue;
			};
			response = responseData == defaultValue ? new Response(ResponseCode.WRONG_TYPE, requestType) : 
					new Response(ResponseCode.OK, responseData);
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
		}
		
		return response;
	}

	private Serializable employee_department_update(Serializable requestData) {
		UpdateDepartmentData data = (UpdateDepartmentData) requestData;
		long id = data.id();
		String department = data.department();
		return company.updateDepartment(id, department);
	}

	private Serializable employees_byAge(Serializable requestData) {
		int[] age = (int[]) requestData;
		return new ArrayList<>(company.getEmployeesByAge(age[0], age[1]));
	}

	private Serializable employees_bySalary(Serializable requestData) {
		int[] salary = (int[])requestData;
		return new ArrayList<>(company.getEmployeesBySalary(salary[0], salary[1]));
	}

	private Serializable employees_byDepartment(Serializable requestData) {
		String department = (String) requestData;
		return new ArrayList<>(company.getEmployeesByDepartment(department));
	}

	private Serializable employees_salary_distribution(Serializable requestData) {
		int interval = (int) requestData;
		return new ArrayList<>(company.getSalaryDistribution(interval));
	}

	private Serializable employees_department_salary_distribution(Serializable requestData) {
		
		return new ArrayList<>(company.getDepartmentSalaryDistribution());
	}

	private Serializable employee_remove(Serializable requestData) {
		long id = (long) requestData;
		return company.removeEmployee(id);
	}

	private Serializable employee_salary_update(Serializable requestData) {
		UpdateSalaryData data = (UpdateSalaryData) requestData;
		long id = data.id();
		int newSalary = data.newSalary();
		return company.updateSalary(id, newSalary);
	}

	private Serializable employees_all(Serializable requestData) {
		
		return new ArrayList<>(company.getEmployees());
	}

	private Serializable employees_get(Serializable requestData) {
		long id = (long) requestData;
		return company.getEmployee(id);
	}

	private Serializable employee_add(Serializable requestData) {
		Employee empl = (Employee) requestData;
		return company.addEmployee(empl);
	}

}
