package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;
import telran.view.InputOutput;
import telran.view.SystemInputOutput;

class InputOutputTest {
	InputOutput io = new SystemInputOutput();
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testReadEmployeeString() {
		Employee empl = io.readObject("Enter employee <id>#<name>#<iso birthday>#<department>#<salary>", "Wrong Employee",
				str -> {
					String[] tokens = str.split("#");
					if(tokens.length != 5) {
						throw new RuntimeException("must be 5 tokens");
					}
					long id = Long.parseLong(tokens[0]);
					String name = tokens[1];
					String department = tokens[3];
					int salary = Integer.parseInt(tokens[4]);
					LocalDate birthday = LocalDate.parse(tokens[2]);
					return new Employee(id, name, department, salary, birthday);
				});
		
		io.writeObjectLine(empl);
	}

}
