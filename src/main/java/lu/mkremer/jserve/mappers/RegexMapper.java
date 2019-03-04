package lu.mkremer.jserve.mappers;

import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;

@Configurable(id = MapperTypes.PATH_MAPPER_REGEX_ID)
public class RegexMapper implements PathMapper {

    @ConfigField(required = true)
    private String pattern;
    @ConfigField(required = true)
    private String to;
    @ConfigField
    private boolean terminal;

    @Override
    public MapperState applies(String path) {
        if (path.matches(pattern)) {
            return terminal ? MapperState.ACCEPT_FINISH : MapperState.ACCEPT_FORWARD;
        }
        return MapperState.NOT_APPLICABLE;
    }

    @Override
    public String map(String path) {
        return path.replaceAll(pattern, to);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
}
