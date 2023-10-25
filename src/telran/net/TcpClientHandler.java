package telran.net;

import java.io.*;
import java.net.*;

public class TcpClientHandler implements Closeable , NetworkHandler {
	Socket socket;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	public TcpClientHandler(String host, int port) throws Exception{
		socket = new Socket(host, port);
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable requestData) {
		Request request = new Request(requestType, requestData);
		try {
			writer.writeObject(request);
			Response response = (Response) reader.readObject();
			if(response.code() != ResponseCode.OK) {
				throw new Exception(response.responseData().toString());
			}
			return (T) response.responseData();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
