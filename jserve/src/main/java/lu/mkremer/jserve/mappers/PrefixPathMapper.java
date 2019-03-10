package lu.mkremer.jserve.mappers;

import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;
import lu.mkremer.jserve.api.mapper.PathMapper;
import lu.mkremer.jserve.api.mapper.state.MapperState;

@Configurable(id = MapperTypes.PATH_MAPPER_PREFIX_ID)
public class PrefixPathMapper implements PathMapper {

	@ConfigField(required = true, name = "from")
	private String fromPrefix;
	@ConfigField(required = true, name = "to")
	private String toPrefix;
	@ConfigField(name = "ignore_case", defaultValue = "false")
	private boolean ignoreCase;
	@ConfigField(defaultValue = "false")
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
