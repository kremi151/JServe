package lu.mkremer.jserve.threading;

import lu.mkremer.jserve.JServeApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketListener implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(SocketListener.class.getName());
	
	private final JServeApplication application;
	
	public SocketListener(JServeApplication application) {
		this.application = application;
	}

	@Override
	public void run() {
		try (ServerSocket socket = new ServerSocket(application.getConfiguration().getPort())) {
			LOGGER.log(Level.INFO, "Server listening on port {0}", application.getConfiguration().getPort());
			LOGGER.log(Level.INFO, "Serve path {0}", application.getConfiguration().getServePath());
			while (true) {
				Socket clientSocket = socket.accept();
				application.getExecutorService().execute(new SocketResponder(clientSocket, application.getConfiguration()));
			}
		} catch (IOException e) {
			throw new RuntimeException("Server has been stopped", e);
		}
	}

}
