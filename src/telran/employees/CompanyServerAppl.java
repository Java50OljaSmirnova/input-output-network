package telran.employees;

import java.util.HashSet;
import java.util.List;

import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;
import telran.net.*;
import telran.view.InputOutput;
import telran.view.SystemInputOutput;

public class CompanyServerAppl {
	
	private static final int PORT = 5000;
	private static final String DEFAULT_FILE_NAME = "employees.data";
	
	public static void main(String[] args) throws Exception{
		String fileName = args.length > 0 ? args[0] : DEFAULT_FILE_NAME;
		Company company = new CompanyImpl();
		company.restore(fileName);
		ApplProtocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(PORT, protocol);
		Thread thread = new Thread(tcpServer);
		thread.start();
		InputOutput io = new SystemInputOutput();
		io.readString("Enter shutdown command for exit", "no shutdown command", new HashSet<String>(List.of("shutdown")));
		tcpServer.shutdown();
		company.save(fileName);
	}
}