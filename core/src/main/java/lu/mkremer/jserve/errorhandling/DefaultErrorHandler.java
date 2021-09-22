package lu.mkremer.jserve.errorhandling;

import java.io.IOException;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;

public class DefaultErrorHandler implements ErrorHandler {

	public static final String JSON_TYPE = "default";
	
	@Override
	public boolean canHandle(int errorCode, String path) {
		return true; 
	}

	@Override
	public void respond(int errorCode, String status, String path, WriteableOutputStream out, ServerConfiguration configuration) throws IOException {
		out.write("HTTP/1.0 ");
		out.write(String.valueOf(errorCode));
		if (status != null) {
			out.write(" ");
			out.write(status);
		}
		out.write("\r\n");
	}

	@Override
	public String getType() {
		return JSON_TYPE;
	}

}
