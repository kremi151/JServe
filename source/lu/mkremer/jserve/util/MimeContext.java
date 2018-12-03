package lu.mkremer.jserve.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lu.mkremer.jserve.io.CSVReader;

public class MimeContext {
	
	private static MimeContext instance = null;

	private Map<String, String> extensionsToMimeTypes = Collections.emptyMap();
	
	private MimeContext() {
		
	}
	
	public void loadMimeTypes(CSVReader reader) throws IOException {
		HashMap<String, String> map = new HashMap<>();
		String row[] = null;
		while ((row = reader.readRow()) != null) {
			map.put(row[0], row[1]);
		}
		extensionsToMimeTypes = Collections.unmodifiableMap(map);
		System.out.format("Loaded %d mime types\n", map.size());
	}
	
	public String getFileExtension(String path) {
		int index = path.lastIndexOf('.');
		if (index >= 0) {
			return path.substring(index + 1);
		} else {
			return null;
		}
	}
	
	public String getMimeType(Path path) throws IOException {
		String mime = extensionsToMimeTypes.get(getFileExtension(path.toString()));
		if (mime == null) {
			mime = Files.probeContentType(path);
		}
		if (mime == null) {
			mime = "application/x-binary";
		}
		return mime;
	}
	
	public static MimeContext getInstance() {
		if (instance == null) {
			instance = new MimeContext();
		}
		return instance;
	}
	
}
