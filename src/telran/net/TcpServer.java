package telran.net;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable {
	private int port;
	private ApplProtocol protocol;
	private ServerSocket serverSocket;

	public TcpServer(int port, ApplProtocol protocol) throws Exception {
		this.port = port;
		this.protocol = protocol;
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		System.out.println("Server is listening on port " + port);
		try {
			while(true) {
				Socket socket = serverSocket.accept();
				TcpClientServer client = new TcpClientServer(socket, protocol);
				client.run();
			}
		}catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

}
