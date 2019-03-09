package lu.mkremer.jserve.util;

import lu.mkremer.jserve.api.JServePlugin;
import lu.mkremer.jserve.api.registration.PathMapperRegistry;
import lu.mkremer.jserve.mappers.GlobMapper;
import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.mappers.PrefixPathMapper;
import lu.mkremer.jserve.mappers.RegexMapper;

public class DefaultJServePlugin extends JServePlugin {

    public DefaultJServePlugin() {
        super("jserve");
    }

    @Override
    public void onRegisterMappers(PathMapperRegistry registry) {
        registry.registerPathMapper(GlobMapper.class, GlobMapper::new);
        registry.registerPathMapper(IndexPathMapper.class, IndexPathMapper::new);
        registry.registerPathMapper(PrefixPathMapper.class, PrefixPathMapper::new);
        registry.registerPathMapper(RegexMapper.class, RegexMapper::new);
    }

}
