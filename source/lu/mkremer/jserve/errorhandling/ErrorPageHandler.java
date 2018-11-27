package lu.mkremer.jserve.errorhandling;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;
import lu.mkremer.jserve.threading.SocketResponder;

public class ErrorPageHandler extends AbstractRangedErrorHandler {
	
	private String path;

	@Override
	public void respond(int errorCode, String path, WriteableOutputStream out, ServerConfiguration configuration) throws IOException {
		File file = new File(configuration.getServePath(), path);
		
		if (!file.exists() || file.isDirectory()) {
			out.write("HTTP/1.0 404 Error page not found\r\n");
			return;
		}
		
		out.write("HTTP/1.0 ");
		out.write(String.valueOf(errorCode));
		out.write("\r\n");
		SocketResponder.serveFile(file, new Date(), out);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
