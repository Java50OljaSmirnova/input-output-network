package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
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
	String[] options = {"QA", "Development", "Audit", "Accounting", "Management"};
	InputOutput io = new SystemInputOutput();
	DoubleBinaryOperator[] operators = {
			(a, b) -> a + b,
			(a, b) -> a - b,
			(a, b) -> a * b,
			(a, b) -> {
				if(b == 0) {
					throw new ArithmeticException("division on 0");
				}
				return a / b;
			}
	};
	char[] operations = {'+', '-', '*','/'};
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	@Order(1)
	@Disabled
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
					LocalDate birthDate = LocalDate.parse(tokens[2]);
					return new Employee(id, name, department, salary, birthDate);
				});
		
		io.writeObjectLine(empl);
	}
	@Test
	@Order(2)
	void testReadEmployeeBySeparateField() {
		long id = io.readLong("Enter employee ID", "Number is less than " + MIN_ID + " or most than " + MAX_ID, MIN_ID, MAX_ID);
		String name = io.readString("Enter employee name",
				"name must be contains more than two letters where first one is a capital", n -> n.matches(EXPRESIONS));
		String department = io.readString("Enter employee department",
				"department must be one out of: QA, Development, Audit, Accounting, Management", getOptions());
		int salary = io.readInt("Enter employee salary", "Number is less than " + MIN_SALARY + " or most than " + MAX_SALARY,
				MIN_SALARY, MAX_SALARY);
		LocalDate birthDate = io.readIsoDate("Enter employee birthDate", "Date is state before " + MIN_DATE + " or after " + MAX_DATE, MIN_DATE, MAX_DATE);
					
		Employee empl = new Employee(id, name, department, salary, birthDate);
		io.writeObjectLine(empl);
	}
	private HashSet<String> getOptions() {
		HashSet<String> res = new HashSet<>();
		for(String str : options) {
			res.add(str);
		}
		return res;
	}

	@Test
	@Order(3)
	void testSimpleArithmeticCalculator() {
		Double res =  io.readObject("Enter expression <number>#<symbol>#<number>", 
				"Wrong expresion", 
				str ->{
					String[] tokens = str.split("#");
					if(tokens.length != 3) {
						throw new RuntimeException("must be 3 tokens");
					}
					int first;
					int second;
					try {
						first = Integer.parseInt(tokens[0]);
						second = Integer.parseInt(tokens[2]);
					} catch (RuntimeException e) {
						throw new RuntimeException(e.getMessage());
					}
					int indexOfSymbol = getIndex(tokens[1]);
					if(indexOfSymbol < 0) {
						throw new RuntimeException("operation " + tokens[1] + " does not exist");
					}
					return operators[indexOfSymbol].applyAsDouble(first, second);
				});
		io.writeObjectLine(res);		
	}

	private int getIndex(String string) {
		char sym = string.charAt(0);
		int index = 0;
		while(operations[index] != sym && index < operations.length) {
			index++;			
		}
		return index < operations.length ? index : -1;
	}

}
