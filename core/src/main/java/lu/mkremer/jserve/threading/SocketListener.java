package lu.mkremer.jserve.threading;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lu.mkremer.jserve.JServe;
import org.tinylog.Logger;

public class SocketListener implements Runnable {
	
	private final JServe application;

	private volatile boolean running = true;
	
	public SocketListener(JServe application) {
		this.application = application;
	}

	@Override
	public void run() {
		try (final ServerSocket socket = new ServerSocket(application.getConfiguration().getPort())) {
			Logger.info("Server listening on port {}", application.getConfiguration().getPort());
			Logger.info("Serve path {}", application.getConfiguration().getServePath());

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				Logger.debug("Shutting down socket listener...");
				running = false;
				tryCloseSocket(socket);
				Logger.debug("Socket listener shut down");
			}));

			while (running) {
				Socket clientSocket = socket.accept();
				application.handleSocket(clientSocket);
			}

			Logger.info("JServe has shut down");
		} catch (IOException e) {
			throw new RuntimeException("Server has been stopped", e);
		}
	}

	private static void tryCloseSocket(ServerSocket socket) {
		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			Logger.warn(e, "An error occurred while closing the socket");
		}
	}

}
