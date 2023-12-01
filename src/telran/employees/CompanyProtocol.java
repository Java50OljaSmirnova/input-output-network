package telran.employees;

import java.io.Serializable;
import java.lang.reflect.Method;
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
		String requestType = request.requestType().replace("/", "_");
		Response response = null;
		Serializable responseData = 0;
		try {
			Method method = CompanyProtocol.class.getDeclaredMethod(requestType, Serializable.class);
			responseData = (Serializable) method.invoke(this, requestData);
			response = new Response(ResponseCode.OK, responseData);
		}catch (NoSuchMethodException e) {
			response = new Response(ResponseCode.WRONG_TYPE, requestType);
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, requestType);
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

	private Serializable employee_get(Serializable requestData) {
		long id = (long) requestData;
		return company.getEmployee(id);
	}

	private Serializable employee_add(Serializable requestData) {
		Employee empl = (Employee) requestData;
		return company.addEmployee(empl);
	}

}
