import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.mappers.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeserializingTest {

    @Tag("fast")
    @Test
    public void registerMappersWithFactoriesTest() {
        PathMapperFactory factory = PathMapperFactory.get();
        factory.reset();
        assertDoesNotThrow(() -> factory.registerPathMapper(GlobMapper.class, GlobMapper::new));
        assertDoesNotThrow(() -> factory.registerPathMapper(IndexPathMapper.class, IndexPathMapper::new));
        assertDoesNotThrow(() -> factory.registerPathMapper(PrefixPathMapper.class, PrefixPathMapper::new));
    }

    @Tag("fast")
    @Test
    public void registerMappersWithoutFactoriesTest() {
        PathMapperFactory factory = PathMapperFactory.get();
        factory.reset();
        assertDoesNotThrow(() -> factory.registerPathMapper(GlobMapper.class));
        assertDoesNotThrow(() -> factory.registerPathMapper(IndexPathMapper.class));
        assertDoesNotThrow(() -> factory.registerPathMapper(PrefixPathMapper.class));
    }

    @Tag("slow")
    @Test
    public void parseGlobMapperTest() {
        PathMapperFactory factory = PathMapperFactory.get();
        factory.reset();
        factory.registerPathMapper(GlobMapper.class, GlobMapper::new);

        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode json = objMapper.createObjectNode();
        json.put("type", MapperTypes.PATH_MAPPER_GLOB_ID);
        json.put("pattern", "/app/**");
        json.put("destination", "/new-path/"); // Legacy property
        // Use default value for "terminal"

        assertDoesNotThrow(() -> {
            GlobMapper mapper = factory.parseMapper(json);

            assertEquals("/app/**", mapper.getPattern());
            assertEquals("/new-path/", mapper.getDestination());
            assertFalse(mapper.isTerminal());
        });

        json.put("terminal", true);
        json.remove("destination");
        json.put("to", "/newer-path/");

        assertDoesNotThrow(() -> {
            GlobMapper mapper = factory.parseMapper(json);

            assertEquals("/app/**", mapper.getPattern());
            assertEquals("/newer-path/", mapper.getDestination());
            assertTrue(mapper.isTerminal());
        });
    }
}
