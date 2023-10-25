package telran.net;

import java.io.IOException;
import java.time.LocalDate;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

//use class TcpClientHandler
public class NumbersDatesTcpClientAppl {
	private static final String HOST = "localhost";
	private static final int PORT = 5000;
	static TcpClientHandler client;

	public static void main(String[] args) throws Exception {
		InputOutput io = new SystemInputOutput();
		client = new TcpClientHandler(HOST, PORT);
		Item[] numberOperationsItems = getNumberOperationsItems();
		Menu numberMenu = new Menu("Number operations", numberOperationsItems);
		Item[] dateOperationsItems = getDateOperationsItems();
		Menu dateMenu = new Menu("Date operations", dateOperationsItems);
		Menu menu = new Menu("Operations-Client", numberMenu, dateMenu, Item.of("Exit", io1 -> {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, true));
		menu.perform(io);

	}

	private static Item[] getNumberOperationsItems() {

		return new Item[] { Item.of("Add two numbers", io1 -> operationOfNumbers("add", io1)),
				Item.of("Subtract two numbers", io1 -> operationOfNumbers("subtract", io1)),
				Item.of("Multiply two numbers", io1 -> operationOfNumbers("multiply", io1)),
				Item.of("Divide two numbers", io1 -> operationOfNumbers("divide", io1)), Item.exit() };
	}

	private static void operationOfNumbers(String type, InputOutput io1) {
		double num1 = io1.readDouble("Enter first number", "Wrong number");
		double num2 = io1.readDouble("Enter second number", "Wrong number");
		String requestData = String.valueOf(num1) + "#" + String.valueOf(num2);
		client.send(type, requestData);
	}

	private static Item[] getDateOperationsItems() {

		return new Item[] {
				Item.of("Date after a given number of days", io1 -> operationOfDates("dateAdding", io1, false)),
				Item.of("Date before a given number of days", io1 -> operationOfDates("dateSubtracting", io1, false)),
				Item.of("Days between to dates", io1 -> operationOfDates("daysBetween", io1, true)), 
				Item.exit() };
	}

	private static void operationOfDates(String type, InputOutput io1, boolean isBetween) {
		LocalDate date1 = io1.readIsoDate("Enter first date in ISO format", "Wrong date");
		LocalDate date2 = null;
		String requestData = "";
		int days = 0;
		if(isBetween) {
		   date2 = io1.readIsoDate("Enter second date in ISO format", "Wrong date");
		   requestData = date1.toString() + "#" + date2.toString();
		} else {
		   days = io1.readInt("Enter number of days", "Wrong number of days", 1, Integer.MAX_VALUE);
		   if(type.equalsIgnoreCase("dateSubtracting")) {
			   days = -days;
		   }
		   requestData = date1.toString() + "#" + String.valueOf(days);
		} 
		client.send(type, requestData);
	}

}
