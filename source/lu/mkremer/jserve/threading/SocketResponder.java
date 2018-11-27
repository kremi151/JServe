package lu.mkremer.jserve.threading;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;
import lu.mkremer.jserve.util.Request;
import lu.mkremer.jserve.util.RequestParser;

public class SocketResponder implements Runnable {
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

	private final Socket socket;
	private final ServerConfiguration configuration;

	public SocketResponder(Socket socket, ServerConfiguration configuration) {
		this.socket = socket;
		this.configuration = configuration;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				WriteableOutputStream out = new WriteableOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
			
			Request request = RequestParser.parseRequest(reader);
			
			Date requestDate = new Date();
			
			final String mappedPath = configuration.mapPath(request.getPath());
			
			File requestedFile = new File(configuration.getServePath(), mappedPath); //TODO: Prevent cross path referencing
			
			System.out.println("Method: " + request.getMethod());
			System.out.println("Path: " + request.getPath() + " -> " + mappedPath);
			
			if (!requestedFile.exists() || requestedFile.isDirectory()) {
				configuration.findErrorHandler(404, mappedPath).respond(404, mappedPath, out, configuration);
				return;
			}

			out.write("HTTP/1.0 200 OK\r\n");
			serveFile(requestedFile, requestDate, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void serveFile(File file, Date requestDate, WriteableOutputStream out) throws IOException {
		Date expireDate = new Date(requestDate.getTime() + 3600000);
		
		out.write("Date: ");
		out.write(dateFormatter.format(requestDate));
		out.write("\r\n");
		out.write("Server: JServe/0.0.1\r\n");
		out.write("Content-Type: ");
		out.write(Files.probeContentType(file.toPath()));
		out.write("\r\n");
		out.write("Content-Length: ");
		out.write(String.valueOf(file.length()));
		out.write("\r\n");
		out.write("Expires: ");
		out.write(dateFormatter.format(expireDate));
		out.write("\r\n");
		out.write("Last-modified: ");
		out.write(dateFormatter.format(new Date(file.lastModified())));
		out.write("\r\n");
		out.write("\r\n");
		byte buffer[] = new byte[2048];
		int length;
		try (FileInputStream fis = new FileInputStream(file)) {
			while ((length = fis.read(buffer)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
		}
		out.write("\r\n");
	}

}
