package telran.net;
import java.net.*;
import java.io.*;
public class ClientSessionHandler implements Runnable {
	Socket socket;
	ObjectInputStream reader;
	ObjectOutputStream writer;
	ApplProtocol protocol;
	TcpServer tcpServer;
	private int startLoadingTime;
	
	public ClientSessionHandler(Socket socket, ApplProtocol protocol, TcpServer tcpServer) throws Exception{
		this.socket = socket;
		this.protocol = protocol;
		this.tcpServer = tcpServer;
		reader = new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		while (!tcpServer.executor.isShutdown()) {
			try {
				Request request = (Request) reader.readObject();
				Response response = protocol.getResponse(request);
				writer.writeObject(response);
				writer.reset();
				
			} catch (SocketTimeoutException e) {
				startLoadingTime += TcpServer.IDLE_TIMEOUT;
				if(startLoadingTime > TcpServer.TOTAL_IDLE_TIME && 
						TcpServer.counterClientsConnected.get() > tcpServer.getnThreads()) {
					try {
						TcpServer.counterClientsConnected.decrementAndGet();
						socket.close();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					System.out.println("total Idle time of client session exceeds a predefined value");
					break;
				}
			} catch (EOFException e) {
				System.out.println("Client closed connection");
			} catch (Exception e) {
				System.out.println("Abnormal clossing connection");
			}
		}

	}

}
