package lu.mkremer.jserve.mappers;

public class IndexPathMapper implements PathMapper {

	public static final String JSON_TYPE = "index";
	
	private String indexFile;
	
	public IndexPathMapper(String indexFile) {
		this.indexFile = indexFile;
	}

	@Override
	public MapperState applies(String path) {
		if (path.endsWith("/")) {
			return MapperState.ACCEPT_FORWARD;
		}
		return MapperState.NOT_APPLICABLE;
	}

	@Override
	public String map(String path) {
		return path + indexFile; 
	}

	@Override
	public String getType() {
		return JSON_TYPE;
	}

	public String getIndexFile() {
		return indexFile;
	}

	public void setIndexFile(String indexFile) {
		this.indexFile = indexFile;
	}

}
