package lu.mkremer.jserve.mappers;

public interface PathMapper {

	/**
	 * Determines whether this mapper applies to the given path
	 * @param path
	 * @return
	 */
	MapperState applies(String path);
	
	/**
	 * Maps the given path to the one on the local file system
	 * @param path
	 * @return
	 */
	String map(String path);
	
}
