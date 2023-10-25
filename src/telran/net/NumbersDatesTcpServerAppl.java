package telran.net;
//use class TcpServer
public class NumbersDatesTcpServerAppl {
	private static final int PORT = 5000;
	
	public static void main(String[] args) throws Exception {
		ApplProtocol protocol = new NumbersDatesProtocol();
		TcpServer server = new TcpServer(PORT, protocol);
		while(true) {
			server.run();
		}
		
	}
	

}

