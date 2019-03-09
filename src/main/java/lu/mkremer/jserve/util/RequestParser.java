package lu.mkremer.jserve.util;

import lu.mkremer.jserve.util.Request.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
	
	private static final String GET_PARAMS_REGEX = "(\\?|\\&)([^=]+)\\=([^&]+)";
	
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
                	parseUrl(args[1], request);
                	//TODO: HTTP/1.1
                }
            }
        }
        
        return request;
	}
	
	public static void parseUrl(String rawUrl, Request request) {
		Pattern pattern = Pattern.compile(GET_PARAMS_REGEX);
		
		Matcher matcher = pattern.matcher(rawUrl);
		HashMap<String, String> params = new HashMap<>();
        while(matcher.find()) {
            params.put(matcher.group(2), matcher.group(3));
        }
        request.setParameters(Collections.unmodifiableMap(params));
        
        request.setPath(rawUrl.replaceAll(GET_PARAMS_REGEX, ""));
	}

}
