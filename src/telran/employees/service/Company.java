package telran.employees.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import telran.employees.dto.*;

public interface Company {
	boolean addEmployee(Employee empl); //adds a given employee object, returns true if added otherwise false (if employee with the id exists)
	Employee removeEmployee(long id); //returns reference to an employee being removed otherwise null (if employee doesn't exist)
	Employee getEmployee(long id);//returns reference to an employee by the given id otherwise null (if employee doesn't exist)
	List<Employee> getEmployees(); //returns list of all employee objects. In the case of none exists it returns empty list
	@SuppressWarnings("unchecked")
	default void restore(String dataFile) throws Exception {
		
		List<Employee> employees = new ArrayList<Employee>();
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(dataFile))){
			employees = (List<Employee>) input.readObject();
			for(Employee empl : employees) {
				addEmployee(empl);
			}
		} catch(RuntimeException e) {
			System.out.println("RuntimeException - " + e);
		}
		
	}
	default void save(String dataFile) throws Exception{
		
		List<Employee> employees = getEmployees();
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dataFile))){
			output.writeObject(employees);
			
		} catch(RuntimeException e) {
			System.out.println("RuntimeException - " + e);
		}
	}
}
