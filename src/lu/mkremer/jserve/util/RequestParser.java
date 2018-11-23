package lu.mkremer.jserve.util;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
	
	public static Request parseRequest(BufferedReader reader) throws IOException {
		Request request = new Request();
		
		String s;
        while ((s = reader.readLine()) != null) {
            if (s.isEmpty()) {
                break;
            }
            if (s.toLowerCase().startsWith("get")) {
            	request.setMethod(Request.Method.GET);
            	String args[] = s.split(" ");
            	request.setPath(args[1]);
            	//TODO: HTTP/1.1
            }
        }
        
        return request;
	}

}
