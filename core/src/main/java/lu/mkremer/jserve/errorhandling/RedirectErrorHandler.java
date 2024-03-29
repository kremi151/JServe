package lu.mkremer.jserve.errorhandling;

import java.io.IOException;

import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;

public class RedirectErrorHandler extends AbstractRangedErrorHandler {

	public static final String JSON_TYPE = "redirect";
	
	private String redirect;

	@Override
	public void respond(int errorCode, String status, String path, WriteableOutputStream out, ServerConfiguration configuration) throws IOException {
		out.write("HTTP/1.0 302 Not available\r\n");
		out.write("Location: ");
		out.write(redirect);
		out.write("\r\n");
	}

	@Override
	public String getType() {
		return JSON_TYPE;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

}
