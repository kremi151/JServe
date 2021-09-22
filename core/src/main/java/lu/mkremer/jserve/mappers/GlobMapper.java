package lu.mkremer.jserve.mappers;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class GlobMapper implements PathMapper {

    public static final String JSON_TYPE = "glob";

    private PathMatcher matcher;
    private String pattern;
    private String destination;
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

    @Override
    public String getType() {
        return JSON_TYPE;
    }

    public String getGlobMatcher() {
        return pattern;
    }

    public void setGlobMatcher(String pattern) {
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
