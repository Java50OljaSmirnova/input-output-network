package telran.net;
//use class TcpServer
public class NumbersDatesTcpServerAppl {
	private static final int PORT = 5000;
	
	public static void main(String[] args) throws Exception {
		
		TcpServer server = new TcpServer(PORT, new NumbersDatesProtocol());
		while(true) {
			server.run();
		}
		
	}
	

}

