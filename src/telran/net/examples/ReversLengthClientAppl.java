package telran.net.examples;

import java.io.*;
import java.net.*;
import telran.view.*;

public class ReversLengthClientAppl {
	private static final String HOST = "localhost";
	private static final int PORT = 5000;
	static BufferedReader reader;
	static PrintStream writer;

	public static void main(String[] args) throws IOException {
		InputOutput io = new SystemInputOutput();
		Socket socket = new Socket(HOST, PORT);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintStream(socket.getOutputStream());
		Menu menu = new Menu("Revers-Length-Client", Item.of("reverse", io1 -> runProtocol("reverse", io1)), 
		    Item.of("length", io1 -> runProtocol("length", io1)), Item.of("Exit", io1 -> {
			try {
				socket.close();
			} catch (IOException e) {
				
			}
		}, true));
		
		menu.perform(io);
	}
	static void runProtocol(String type, InputOutput io1) {
		String str = io1.readString("Enter any string");
		writer.printf("%s#%s\n",type, str);
		try {
			io1.writeLine(reader.readLine());
		} catch (IOException e) {
			
		}
	}

}
