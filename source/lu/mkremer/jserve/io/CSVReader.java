package lu.mkremer.jserve.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class CSVReader extends BufferedReader {

	public CSVReader(Reader in) {
		super(in);
	}
	
	public String[] readRow() throws IOException {
		String line = readLine();
		if (line == null) {
			return null;
		}
		return line.split(",");
	}

}
