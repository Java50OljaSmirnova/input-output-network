package telran.net.examples;

import java.io.*;
import java.net.*;
import java.time.LocalDate;

import telran.view.*;

public class NumbersDatesOperationsClient {

	private static final String HOST = "localhost";
	private static final int PORT = 5000;
	static BufferedReader reader;
	static PrintStream writer;

	public static void main(String[] args) throws IOException {
		InputOutput io = new SystemInputOutput();
		Socket socket = new Socket(HOST, PORT);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintStream(socket.getOutputStream());
		Item[] numberOperationsItems = getNumberOperationsItems();
		Menu numberMenu = new Menu("Number operations", numberOperationsItems);
		Item[] dateOperationsItems = getDateOperationsItems();
		Menu dateMenu = new Menu("Date operations", dateOperationsItems);
		Menu menu = new Menu("Operations-Client", numberMenu, dateMenu, Item.of("Exit", io1 -> {
			try {
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}, true));
		menu.perform(io);

	}

	private static Item[] getNumberOperationsItems() {
		
		return new Item[] {
				Item.of("Add two numbers", io1 -> runProtocolOfNumbers("add", io1) ),
				Item.of("Subtract two numbers", io1 -> runProtocolOfNumbers("subtract", io1)),
				Item.of("Multiply two numbers",io1 -> runProtocolOfNumbers("multiply", io1)),
				Item.of("Divide two numbers", io1 -> runProtocolOfNumbers("divide", io1)),
				Item.exit()
		};
	}

	private static void runProtocolOfNumbers(String type, InputOutput io1) {
		double num1 = io1.readDouble("Enter first number", "Wrong number");
		double num2 = io1.readDouble("Enter second number", "Wrong number");
		writer.printf("%s#%s#%s\n", type, String.valueOf(num1), String.valueOf(num2));
		try {
			io1.writeLine(reader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	private static Item[] getDateOperationsItems() {
		
		return new Item[] {
				Item.of("Date after a given number of days", io1 -> runProtocolOfDates("dateAdding", io1, false)),
				Item.of("Date before a given number of days",  io1 -> runProtocolOfDates("dateSubtracting", io1, false)),
				Item.of("Days between to dates",  io1 -> runProtocolOfDates("daysBetween", io1, true)),
				Item.exit()
		};
	}

	private static void runProtocolOfDates(String type, InputOutput io1, boolean isBetween) {
		LocalDate date1 = io1.readIsoDate("Enter first date in ISO format", "Wrong date");
		LocalDate date2 = null;
		int days = 0;
		if(isBetween) {
		   date2 = io1.readIsoDate("Enter second date in ISO format", "Wrong date");
		   writer.printf("%s#%s#%s%n", type, date1.toString(), date2.toString());
			try {
				io1.writeLine(reader.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		} else {
		   days = io1.readInt("Enter number of days", "Wrong number of days", 1, Integer.MAX_VALUE);
		   writer.printf("%s#%s#%s%n", type, date1.toString(), String.valueOf(days));
			try {
				io1.writeLine(reader.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
