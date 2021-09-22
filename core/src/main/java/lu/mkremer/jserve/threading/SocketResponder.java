package lu.mkremer.jserve.threading;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;
import lu.mkremer.jserve.util.BuildVersion;
import lu.mkremer.jserve.util.MimeContext;
import lu.mkremer.jserve.util.Request;
import lu.mkremer.jserve.util.RequestParser;
import lu.mkremer.jserve.util.Request.Method;
import org.tinylog.Logger;

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

			if (request.getMethod() != Method.GET) {
				respondWithError(405, "Method not allowed", request, request.getPath(), out);
				return;
			}

			Date requestDate = new Date();

			final String mappedPath = configuration.mapPath(request.getPath());

			Path requestedPath = Paths.get(configuration.getServePath(), mappedPath);
			if (!requestedPath.startsWith(configuration.getServeNioPath())) {
				respondWithError(406, "Not acceptable", request, mappedPath, out);
				return;
			}

			File requestedFile = requestedPath.toFile();

			if (!requestedFile.exists() || requestedFile.isDirectory()) {
				respondWithError(404, "Not found", request, mappedPath, out);
				return;
			}

			Logger.trace("{} 200 {} -> {} ({}:{})", request.getMethod().name(), request.getPath(), mappedPath, socket.getInetAddress().getHostName(), socket.getPort());

			out.write("HTTP/1.0 200 OK\r\n");
			serveFile(requestedFile, requestDate, out);
			out.flush();
		} catch (SocketException e) {
			Logger.debug(e, "A socket error occurred");
		} catch (IOException e) {
			Logger.error(e, "An I/O error occurred");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				Logger.trace(e, "Closing socket failed");
			}
		}

	}
	
	private void respondWithError(int code, String status, Request request, String mappedPath, WriteableOutputStream out) throws IOException {
		Logger.warn("{} {} {} -> {} ({}:{})", request.getMethod().name(), code, request.getPath(), mappedPath, socket.getInetAddress().getHostName(), socket.getPort());
		configuration.findErrorHandler(code, mappedPath).respond(code, status, mappedPath, out, configuration);
	}
	
	public static void serveFile(File file, Date requestDate, WriteableOutputStream out) throws IOException {
		Date expireDate = new Date(requestDate.getTime() + 3600000);
		
		out.write("Date: ");
		out.write(dateFormatter.format(requestDate));
		out.write("\r\n");
		out.write("Server: JServe/");
		out.write(BuildVersion.VERSION);
		out.write("\r\n");
		out.write("Content-Type: ");
		out.write(MimeContext.getInstance().getMimeType(file.toPath()));
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
