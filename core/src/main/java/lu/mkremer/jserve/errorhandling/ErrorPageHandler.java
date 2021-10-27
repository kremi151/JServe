package lu.mkremer.jserve.errorhandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;
import lu.mkremer.jserve.threading.SocketResponder;

public class ErrorPageHandler extends AbstractRangedErrorHandler {

	public static final String JSON_TYPE = "page";
	
	private String path;

	@Override
	public void respond(int errorCode, String status, String inPath, WriteableOutputStream out, ServerConfiguration configuration) throws IOException {
		Path path = Paths.get(configuration.getServePath(), inPath);
		
		if (!Files.exists(path) || Files.isDirectory(path)) {
			out.write("HTTP/1.0 404 Not found\r\n");
			return;
		}
		
		out.write("HTTP/1.0 ");
		out.write(String.valueOf(errorCode));
		if (status != null) {
			out.write(" ");
			out.write(status);
		}
		out.write("\r\n");
		SocketResponder.serveFile(path, new Date(), out);
	}

	@Override
	public String getType() {
		return JSON_TYPE;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
