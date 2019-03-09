package lu.mkremer.jserve.api.registration;

import lu.mkremer.jserve.api.util.DefaultConstructorFactory;
import lu.mkremer.jserve.mappers.PathMapper;

import java.util.function.Supplier;

public interface PathMapperRegistry {

    <M extends PathMapper> void registerPathMapper(Class<M> clazz, Supplier<M> factory);

    @Deprecated
    default <M extends PathMapper> void registerPathMapper(Class<M> clazz) {
        registerPathMapper(clazz, new DefaultConstructorFactory<>(clazz));
    }

}
