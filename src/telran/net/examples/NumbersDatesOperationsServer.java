package telran.net.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NumbersDatesOperationsServer {

	private static final int PORT = 5000;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server is listening on port " + PORT);
		while(true) {
			Socket socket = serverSocket.accept();
			runProtocol(socket);
		}

	}

	private static void runProtocol(Socket socket) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream writer = new PrintStream(socket.getOutputStream());){
			boolean running = true;
			while(running) {
				String request = reader.readLine();
				if(request == null) {
					System.out.println("client" + socket.getRemoteSocketAddress() + "closed connection");
					running = false;
				} else {
					String response = getResponse(request);
					writer.println(response);
				}
			}
		}catch(Exception e) {
			System.out.println("Abnormal socket clossing");
		}
		
	}

	private static String getResponse(String request) {
		String response = null;
		String[] tokens = request.split("#");
		if(tokens.length != 3) {
			response = "request must be in format <type>#<string>#<string>";
		} else {
			response = switch(tokens[0]) {
			case "add" -> Double.toString(Double.parseDouble(tokens[1]) + Double.parseDouble(tokens[2]));
			case "subtract" -> Double.toString(Double.parseDouble(tokens[1]) - Double.parseDouble(tokens[2]));
			case "multiply" -> Double.toString(Double.parseDouble(tokens[1]) * Double.parseDouble(tokens[2]));
			case "divide" -> Double.toString(Double.parseDouble(tokens[1]) / Double.parseDouble(tokens[2]));
			case "dateAdding" -> LocalDate.parse((tokens[1])).plusDays(Integer.parseInt(tokens[2])).toString();
			case "dateSubtracting" -> LocalDate.parse((tokens[1])).plusDays(-(Integer.parseInt(tokens[2]))).toString();
			case "daysBetween" ->  Long.toString(ChronoUnit.DAYS.between(LocalDate.parse(tokens[1]), LocalDate.parse(tokens[2])));
			default -> "Wrong type";
			};
		}
		return response;
	}
	

}
