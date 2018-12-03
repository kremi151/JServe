package lu.mkremer.jserve.util;

import java.util.Collections;
import java.util.Map;

public class Request {

	private Method method;
	private String path;
	private Map<String, String> parameters = Collections.emptyMap();

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public static enum Method {
		GET,
		POST,
		PUT,
		DELETE,
		PATCH
	}
}
