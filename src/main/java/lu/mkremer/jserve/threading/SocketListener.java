package lu.mkremer.jserve.threading;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lu.mkremer.jserve.JServeApplication;

public class SocketListener implements Runnable {
	
	private final JServeApplication application;
	
	public SocketListener(JServeApplication application) {
		this.application = application;
	}

	@Override
	public void run() {
		try (ServerSocket socket = new ServerSocket(application.getConfiguration().getPort())) {
			System.out.format("Server listening on port %d\n", application.getConfiguration().getPort());
			System.out.format("Serve path %s\n", application.getConfiguration().getServePath().toString());
			while (true) {
				Socket clientSocket = socket.accept();
				application.getExecutorService().execute(new SocketResponder(clientSocket, application.getConfiguration()));
			}
		} catch (IOException e) {
			throw new RuntimeException("Server has been stopped", e);
		}
	}

}
