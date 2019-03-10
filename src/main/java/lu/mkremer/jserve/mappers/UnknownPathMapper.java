package lu.mkremer.jserve.mappers;

import lu.mkremer.jserve.api.mapper.PathMapper;
import lu.mkremer.jserve.api.mapper.state.MapperState;

public class UnknownPathMapper implements PathMapper {

	@Override
	public MapperState applies(String path) {
		return MapperState.NOT_APPLICABLE;
	}

	@Override
	public String map(String path) {
		return path;
	}

}
