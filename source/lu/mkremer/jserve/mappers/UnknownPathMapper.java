package lu.mkremer.jserve.mappers;

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
