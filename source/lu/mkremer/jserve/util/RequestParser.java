package lu.mkremer.jserve.util;

import java.io.BufferedReader;
import java.io.IOException;

import lu.mkremer.jserve.util.Request.Method;

public class RequestParser {
	
	public static Request parseRequest(BufferedReader reader) throws IOException {
		Request request = new Request();
		
		String s;
        while ((s = reader.readLine()) != null) {
            if (s.isEmpty()) {
                break;
            }
            for (Method method : Method.values()) {
            	if (s.toUpperCase().startsWith(method.name())) {
                	request.setMethod(method);
                	String args[] = s.split(" ");
                	request.setPath(args[1]);
                	//TODO: HTTP/1.1
                }
            }
        }
        
        return request;
	}

}
