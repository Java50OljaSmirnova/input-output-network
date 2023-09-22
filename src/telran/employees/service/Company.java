package telran.employees.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
		
		if(Files.exists(Path.of(dataFile))) {
			try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(dataFile))) {
				List<Employee> employeesRestore = (List<Employee>) stream.readObject();
				employeesRestore.forEach(e -> addEmployee(e));
			}catch(Exception e) {
				throw new RuntimeException(e.toString());
			}
		}
		
	}
	default void save(String dataFile) throws Exception{
		
		try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(dataFile))) {
			stream.writeObject(getEmployees());
		}catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
}
