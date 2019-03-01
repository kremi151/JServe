package lu.mkremer.jserve.mappers;

import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;

@Configurable(id = MapperTypes.PATH_MAPPER_INDEX_ID)
public class IndexPathMapper implements PathMapper {

	@ConfigField(name = "file")
	private String indexFile;
	
	public IndexPathMapper(String indexFile) {
		this.indexFile = indexFile;
	}

	public IndexPathMapper() {}

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

	public String getIndexFile() {
		return indexFile;
	}

	public void setIndexFile(String indexFile) {
		this.indexFile = indexFile;
	}

}
