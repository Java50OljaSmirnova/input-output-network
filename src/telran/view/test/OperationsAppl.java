package telran.view.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

public class OperationsAppl {

	public static void main(String[] args) {
		Item[] numberOperationsItems = getNumberOperationsItems();
		Menu numberMenu = new Menu("Number operations", numberOperationsItems);
		Item[] dateOperationsItems = getDateOperationsItems();
		Menu dateMenu = new Menu("Date operations", dateOperationsItems);
		Menu menu = new Menu("Operations", numberMenu, dateMenu, Item.exit());
		menu.perform(new SystemInputOutput());

	}

	private static Item[] getDateOperationsItems() {
		
		return new Item[] {
				Item.of("Date after a given number of days", OperationsAppl::dateAfter),
				Item.of("Date before a given number of days", OperationsAppl::dateBefore),
				Item.of("Days between to dates", OperationsAppl::dayBetween),
				Item.exit()
		};
	}
	static void dateAfter(InputOutput io) {
		LocalDate currentDate = LocalDate.now();
		Long numberOfDays =  io.readLong("Enter number of days", "Wrong number");
		LocalDate dateAfter = currentDate.plusDays(numberOfDays);
		io.writeObjectLine("Date after " + numberOfDays + " days - " + dateAfter);
	}
	static void dateBefore(InputOutput io) {
		LocalDate currentDate = LocalDate.now();
		long numberOfDays =  io.readLong("Enter number of days", "Wrong number");
		LocalDate dateBefore = currentDate.minusDays(numberOfDays);
		io.writeObjectLine("Date before " + numberOfDays + " days - " + dateBefore);
	}
	static void dayBetween(InputOutput io) {
		LocalDate[] dates = getDates(io);
		long days = ChronoUnit.DAYS.between(dates[0], dates[1]);
		io.writeObjectLine("Number of days between " + dates[0] + " & " + dates[1] + " - " + days + " days");
	}

	private static LocalDate[] getDates(InputOutput io) {
		
		return new LocalDate[] {
				io.readIsoDate("Enter first date [yyyy-mm-dd]", "Wrong date"),
				io.readIsoDate("Enter second date [yyyy-mm-dd]", "Wrong date"),
		};
	}

	private static Item[] getNumberOperationsItems() {
		return new Item[] {
				Item.of("Add two numbers", ArithmeticCalculatorAppl::addItem),
				Item.of("Subtract two numbers", ArithmeticCalculatorAppl::subtractItem),
				Item.of("Multiply two numbers", ArithmeticCalculatorAppl::multiplyItem),
				Item.of("Divide two numbers", ArithmeticCalculatorAppl::divideItem),
				Item.exit()
		};
	}
	static void addItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] + operands[1]);
	}
	static void subtractItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] - operands[1]);
	}
	static void multiplyItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] * operands[1]);
	}
	static void divideItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] / operands[1]);
	}

	private static double[] getOperands(InputOutput io) {
		
		return new double[] {
				io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number")
		};
	}

}
