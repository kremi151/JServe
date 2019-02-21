package lu.mkremer.jserve.mappers;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class GlobMapper implements PathMapper {

    private String globMatcher;
    private String destination;
    private boolean terminal;

    public GlobMapper(String globMatcher, String destination, boolean terminal) {
        this.globMatcher = "glob:" + globMatcher;
        this.destination = destination;
        this.terminal = terminal;
    }

    public GlobMapper() {
        this("", "", false);
    }

    @Override
    public MapperState applies(String path) {
        return FileSystems
                .getDefault()
                .getPathMatcher(globMatcher)
                .matches(Paths.get(path)) ? getAcceptance() : MapperState.NOT_APPLICABLE;
    }

    private MapperState getAcceptance() {
        return this.terminal ? MapperState.ACCEPT_FINISH : MapperState.ACCEPT_FORWARD;
    }

    @Override
    public String map(String path) {
        return destination;
    }

    public String getGlobMatcher() {
        return globMatcher;
    }

    public void setGlobMatcher(String globMatcher) {
        this.globMatcher = globMatcher;
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
