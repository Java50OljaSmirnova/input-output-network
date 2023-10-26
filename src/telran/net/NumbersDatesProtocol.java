package telran.net;

import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class NumbersDatesProtocol implements ApplProtocol {
	private String type;
	Serializable data;
	Response response;

	@Override
	public Response getResponse(Request request) {
		type = request.requestType();
		response = switch(type) {
		case "add" -> getSolutionOfNumbersOperation(request.requestData(), "+");
		case "subtract" -> getSolutionOfNumbersOperation(request.requestData(), "-");
		case "multiply" -> getSolutionOfNumbersOperation(request.requestData(), "*");
		case "divide" -> getSolutionOfNumbersOperation(request.requestData(), "/");
		case "dateAdding" -> getSolutionOfDatesOperation(request.requestData(), false);
		case "dateSubtracting" -> getSolutionOfDatesOperation(request.requestData(), false);
		case "daysBetween" -> getSolutionOfDatesOperation(request.requestData(), true);
		default -> new Response(ResponseCode.WRONG_TYPE, "Wrong type");
		};
		return response;
	}

	private Response getSolutionOfDatesOperation(Serializable requestData, boolean isBetween) {
		Response res = null;
		String[] tokens = requestData.toString().split("#");
		if(tokens.length != 2) {
			res = new Response(ResponseCode.WRONG_DATA, "request must be in format <type>#<string>#<string>");
		} else {
			LocalDate date1 = LocalDate.parse(tokens[0]);
			if(isBetween) {
				LocalDate date2 = LocalDate.parse(tokens[1]);
				res = new Response(ResponseCode.OK, Long.toString(ChronoUnit.DAYS.between(date1, date2)));
			} else {
				int days = Integer.parseInt(tokens[1]);
				res = new Response(ResponseCode.OK, date1.plusDays(days).toString());
			}
		}
		return res;
	}

	private Response getSolutionOfNumbersOperation(Serializable requestData, String symbol) {
		Response res = null;
		String[] tokens = requestData.toString().split("#");
		if(tokens.length != 2) {
			res = new Response(ResponseCode.WRONG_DATA, "request must be in format <type>#<string>#<string>");
		} else {
			double num1 = Double.parseDouble(tokens[0]);
			double num2 = Double.parseDouble(tokens[1]);
			res = switch(symbol) {
			case "+" -> new Response(ResponseCode.OK, Double.toString(num1 + num2));
			case "-" -> new Response(ResponseCode.OK, Double.toString(num1 - num2));
			case "*" -> new Response(ResponseCode.OK, Double.toString(num1 * num2));
			case "/" -> new Response(ResponseCode.OK, Double.toString(num1 / num2));
			default -> new Response(ResponseCode.WRONG_DATA, "Wrong operation");
			};
		}
		return res;
	}

}
