package telran.net;
import java.net.*;
import java.io.*;
public class ClientSessionHandler implements Runnable {
	private static final int TOTAL_IDLE_TIME = 30000;
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
				if(startLoadingTime > TOTAL_IDLE_TIME && 
						tcpServer.counterClientsConnected.get() > tcpServer.nThreads) {
					break;
				}
			} catch (EOFException e) {
				System.out.println("Client closed connection");
				break;
			} catch (Exception e) {
				System.out.println("Abnormal clossing connection");
				break;
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}finally {
			tcpServer.counterClientsConnected.decrementAndGet();
		}

	}

}
