package lu.mkremer.jserve.util;

public class Request {

	private Method method;
	private String path;

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

	public static enum Method {
		GET
	}
}
