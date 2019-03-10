package lu.mkremer.jserve.api.mapper;

import lu.mkremer.jserve.api.mapper.state.MapperState;

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
