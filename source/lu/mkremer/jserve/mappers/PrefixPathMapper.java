package lu.mkremer.jserve.mappers;

public class PrefixPathMapper implements PathMapper {
	
	private final String fromPrefix;
	private final String toPrefix;
	private final boolean ignoreCase;
	private final boolean terminal;
	
	public PrefixPathMapper(String fromPrefix, String toPrefix, boolean ignoreCase, boolean terminal) {
		this.fromPrefix = fromPrefix;
		this.toPrefix = toPrefix;
		this.ignoreCase = ignoreCase;
		this.terminal = terminal;
	}

	@Override
	public MapperState applies(String path) {
		if ((ignoreCase && path.toLowerCase().startsWith(fromPrefix.toLowerCase()))
				|| path.startsWith(fromPrefix.toLowerCase())) {
			return terminal ? MapperState.ACCEPT_FINISH : MapperState.ACCEPT_FORWARD;
		}
		return MapperState.NOT_APPLICABLE;
	}

	@Override
	public String map(String path) {
		return toPrefix + path.substring(fromPrefix.length());
	}

}
