package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.Employee;
import telran.view.InputOutput;
import telran.view.SystemInputOutput;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InputOutputTest {
	private static final long MIN_ID = 100_000;
	private static final long MAX_ID = 999_999;
	private static final String EXPRESIONS = "[A-Z][a-z]+";
	private static final int MIN_SALARY = 7_000;
	private static final int MAX_SALARY = 50_000;
	private static final LocalDate MIN_DATE = LocalDate.parse("1950-12-31");
	private static final LocalDate MAX_DATE = LocalDate.parse("2003-12-31");
	String[] options = { "QA", "Development", "Audit", "Accounting", "Management" };
	InputOutput io = new SystemInputOutput();
	
	HashSet<String> operations = new HashSet<>(List.of("+","-","*","/"));

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	@Order(1)
	@Disabled
	void testReadEmployeeString() {
		Employee empl = io.readObject("Enter employee <id>#<name>#<iso birthday>#<department>#<salary>",
				"Wrong Employee", str -> {
					String[] tokens = str.split("#");
					if (tokens.length != 5) {
						throw new RuntimeException("must be 5 tokens");
					}
					long id = Long.parseLong(tokens[0]);
					String name = tokens[1];
					String department = tokens[3];
					int salary = Integer.parseInt(tokens[4]);
					LocalDate birthDate = LocalDate.parse(tokens[2]);
					return new Employee(id, name, department, salary, birthDate);
				});

		io.writeObjectLine(empl);
	}

	@Test
	@Order(3)
	void testReadEmployeeBySeparateField() {
		long id = io.readLong("Enter employee ID", "Wrong identity value", MIN_ID, MAX_ID);
		String name = io.readString("Enter employee name", "Wrong name", n -> n.matches(EXPRESIONS));
		String department = io.readString("Enter employee department", "Wrong department", getOptions());
		int salary = io.readInt("Enter employee salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		LocalDate birthDate = io.readIsoDate("Enter employee birthDate",
				"Wrong birthdate", MIN_DATE, MAX_DATE);

		io.writeObjectLine(new Employee(id, name, department, salary, birthDate));
	}

	private HashSet<String> getOptions() {
		HashSet<String> res = new HashSet<>();
		for (String str : options) {
			res.add(str);
		}
		return res;
	}

	@Test
	@Order(2)
	void testSimpleArithmeticCalculator() {
		
		double op1 = io.readDouble("Enter first number", "Wrong number");
		double op2 = io.readDouble("Enter second number", "Wrong number");
		String operation = io.readString("Enter operation from " + operations, "Wrong operation", operations);
					
		double res = 
		switch(operation) {
		case "+" -> op1 + op2;
		case "-" -> op1 - op2;
		case "*" -> op1 * op2;
		case "/" -> op1 / op2;
		default -> throw new IllegalArgumentException("Unexpected value: " + operation);

			};
		io.writeObjectLine(res);

	}

}
