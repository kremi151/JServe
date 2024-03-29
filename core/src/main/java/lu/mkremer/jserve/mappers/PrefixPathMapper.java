package lu.mkremer.jserve.mappers;

public class PrefixPathMapper implements PathMapper {

	public static final String JSON_TYPE = "prefix";
	
	private String fromPrefix;
	private String toPrefix;
	private boolean ignoreCase;
	private boolean terminal;
	
	public PrefixPathMapper(String fromPrefix, String toPrefix, boolean ignoreCase, boolean terminal) {
		this.fromPrefix = fromPrefix;
		this.toPrefix = toPrefix;
		this.ignoreCase = ignoreCase;
		this.terminal = terminal;
	}
	
	public PrefixPathMapper() {
		this("", "", false, false);
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

	@Override
	public String getType() {
		return JSON_TYPE;
	}

	public String getFromPrefix() {
		return fromPrefix;
	}

	public void setFromPrefix(String fromPrefix) {
		this.fromPrefix = fromPrefix;
	}

	public String getToPrefix() {
		return toPrefix;
	}

	public void setToPrefix(String toPrefix) {
		this.toPrefix = toPrefix;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

}
