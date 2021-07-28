package lu.mkremer.jserve.threading;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lu.mkremer.jserve.JServe;
import org.tinylog.Logger;

import javax.security.auth.login.Configuration;

public class SocketListener implements Runnable {
	
	private final JServe application;
	
	public SocketListener(JServe application) {
		this.application = application;
	}

	@Override
	public void run() {
		try (ServerSocket socket = new ServerSocket(application.getConfiguration().getPort())) {
			Logger.info("Server listening on port {}", application.getConfiguration().getPort());
			Logger.info("Serve path {}", application.getConfiguration().getServePath());
			while (true) {
				Socket clientSocket = socket.accept();
				application.getExecutorService().execute(new SocketResponder(clientSocket, application.getConfiguration()));
			}
		} catch (IOException e) {
			throw new RuntimeException("Server has been stopped", e);
		}
	}

}
