package lu.mkremer.jserve.mappers;

import java.util.Collections;
import java.util.List;

public class ChainMapper implements PathMapper {

    public static final String JSON_TYPE = "chain";

    private boolean terminal;
    private List<PathMapper> mappers;

    public ChainMapper(List<PathMapper> mappers, boolean terminal) {
        this.mappers = mappers;
        this.terminal = terminal;
    }

    public ChainMapper() {
        this(Collections.emptyList(), false);
    }

    @Override
    public MapperState applies(String path) {
        return terminal
                ? MapperState.ACCEPT_FINISH
                : MapperState.ACCEPT_FORWARD;
    }

    @Override
    public String map(String path) {
        if (mappers == null) {
            return path;
        }
        for (PathMapper mapper : mappers) {
            MapperState state = mapper.applies(path);
            if (!state.isApplicable()) {
                continue;
            }
            path = mapper.map(path);
            if (state.isTerminal()) {
                return path;
            }
        }
        return path;
    }

    @Override
    public String getType() {
        return JSON_TYPE;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public List<PathMapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<PathMapper> mappers) {
        this.mappers = mappers;
    }

}
