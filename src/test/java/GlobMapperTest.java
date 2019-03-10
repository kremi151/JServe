import lu.mkremer.jserve.mappers.GlobMapper;
import lu.mkremer.jserve.api.mapper.state.MapperState;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobMapperTest {

    @Tag("fast")
    @Test
    public void testGlobPatternNonTerminal() {
        GlobMapper globMapper = new GlobMapper("/api/**", "/other-page", false);
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/abc/some/invalid/path.html"));
        assertEquals(MapperState.ACCEPT_FORWARD, globMapper.applies("/api/some/valid/path.html"));
        assertEquals("/other-page", globMapper.map("/api/some/other/valid/path.html"));
    }

    @Tag("fast")
    @Test
    public void testGlobPatternTerminal() {
        GlobMapper globMapper = new GlobMapper("/abc/**", "/other-page", true);
        assertEquals(MapperState.ACCEPT_FINISH, globMapper.applies("/abc/some/valid/path.html"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/api/some/invalid/path.html"));
        assertEquals("/other-page", globMapper.map("/abc/some/other/valid/path.html"));
    }

    @Tag("fast")
    @Test
    public void testGlobPatternWithExtension() {
        GlobMapper globMapper = new GlobMapper("/root/**/*.{html,htm}", "/test-page", false);
        assertEquals(MapperState.ACCEPT_FORWARD, globMapper.applies("/root/sub/valid.html"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/sub/invalid.xhtml"));
        assertEquals(MapperState.ACCEPT_FORWARD, globMapper.applies("/root/sub/valid.htm"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/sub/invalid.jsp"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/invalid/sub/valid.html"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/invalid/sub/invalid.xhtml"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/invalid/sub/valid.htm"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/invalid/sub/invalid.jsp"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/valid.html"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/invalid.xhtml"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/valid.htm"));
        assertEquals(MapperState.NOT_APPLICABLE, globMapper.applies("/root/invalid.jsp"));
    }
}
