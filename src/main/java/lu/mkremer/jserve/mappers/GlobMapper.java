package lu.mkremer.jserve.mappers;

import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

@Configurable(id = MapperTypes.PATH_MAPPER_GLOB_ID)
public class GlobMapper implements PathMapper {

    private PathMatcher matcher;

    @ConfigField(required = true)
    private String pattern;
    @ConfigField(required = true, name = { "to", "destination" })
    private String destination;
    @ConfigField(defaultValue = "false")
    private boolean terminal;

    public GlobMapper(String pattern, String destination, boolean terminal) {
        this.pattern = pattern;
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        this.destination = destination;
        this.terminal = terminal;
    }

    public GlobMapper() {
        this("", "", false);
    }

    @Override
    public MapperState applies(String path) {
        return matcher.matches(Paths.get(path)) ? getAcceptance() : MapperState.NOT_APPLICABLE;
    }

    private MapperState getAcceptance() {
        return this.terminal ? MapperState.ACCEPT_FINISH : MapperState.ACCEPT_FORWARD;
    }

    @Override
    public String map(String path) {
        return destination;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        this.pattern = pattern;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

}
