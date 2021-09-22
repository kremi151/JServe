package lu.mkremer.jserve.mappers;

import java.util.Collections;
import java.util.List;

public class FirstMatchingMapper implements PathMapper {

    public static final String JSON_TYPE = "first";

    private List<PathMapper> mappers;

    public FirstMatchingMapper(List<PathMapper> mappers) {
        this.mappers = mappers;
    }

    public FirstMatchingMapper() {
        this(Collections.emptyList());
    }

    @Override
    public MapperState applies(String path) {
        if (mappers == null) {
            return MapperState.NOT_APPLICABLE;
        }
        for (PathMapper mapper : mappers) {
            MapperState result = mapper.applies(path);
            if (result.isApplicable()) {
                return result;
            }
        }
        return MapperState.NOT_APPLICABLE;
    }

    @Override
    public String map(String path) {
        if (mappers == null) {
            return path;
        }
        for (PathMapper mapper : mappers) {
            if (mapper.applies(path).isApplicable()) {
                return mapper.map(path);
            }
        }
        return path;
    }

    @Override
    public String getType() {
        return JSON_TYPE;
    }

    public List<PathMapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<PathMapper> mappers) {
        this.mappers = mappers;
    }
}
