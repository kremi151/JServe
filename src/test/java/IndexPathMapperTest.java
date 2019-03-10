import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.api.mapper.state.MapperState;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexPathMapperTest {

    @Tag("fast")
    @Test
    public void textIndexMapping() {
        IndexPathMapper mapper = new IndexPathMapper("index.xhtml");
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/page2.html"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/page5.xml"));
        assertEquals(MapperState.ACCEPT_FORWARD, mapper.applies("/"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/sub/path/page3.htm"));
        assertEquals(MapperState.ACCEPT_FORWARD, mapper.applies("/sub/path/"));
        assertEquals("/index.xhtml", mapper.map("/"));
        assertEquals("/sub/path/index.xhtml", mapper.map("/sub/path/"));
    }

}
