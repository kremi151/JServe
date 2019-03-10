import lu.mkremer.jserve.api.mapper.state.MapperState;
import lu.mkremer.jserve.mappers.PrefixPathMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrefixMapperTest {

    @Tag("fast")
    @Test
    public void prefixMapperTestIgnoreCaseTerminal() {
        PrefixPathMapper mapper = new PrefixPathMapper("/from", "/to", true, true);
        assertEquals(MapperState.ACCEPT_FINISH, mapper.applies("/from/here.txt"));
        assertEquals(MapperState.ACCEPT_FINISH, mapper.applies("/FRom/HERE.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/stop/there.txt"));
    }

    @Tag("fast")
    @Test
    public void prefixMapperTestIgnoreCaseNotTerminal() {
        PrefixPathMapper mapper = new PrefixPathMapper("/from", "/to", true, false);
        assertEquals(MapperState.ACCEPT_FORWARD, mapper.applies("/from/here.txt"));
        assertEquals(MapperState.ACCEPT_FORWARD, mapper.applies("/FRom/HERE.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/stop/there.txt"));
    }

    @Tag("fast")
    @Test
    public void prefixMapperTestTerminal() {
        PrefixPathMapper mapper = new PrefixPathMapper("/from", "/to", false, true);
        assertEquals(MapperState.ACCEPT_FINISH, mapper.applies("/from/here.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/FRom/HERE.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/stop/there.txt"));
    }

    @Tag("fast")
    @Test
    public void prefixMapperTestNotTerminal() {
        PrefixPathMapper mapper = new PrefixPathMapper("/from", "/to", false, false);
        assertEquals(MapperState.ACCEPT_FORWARD, mapper.applies("/from/here.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/FRom/HERE.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/stop/there.txt"));
    }

    @Tag("fast")
    @Test
    public void mapPrefixPathText() {
        PrefixPathMapper mapper = new PrefixPathMapper("/from", "/to", true, true);
        assertEquals("/to/here.png", mapper.map("/from/here.png"));
        assertEquals("/to/from/until/when.txt", mapper.map("/from/from/until/when.txt"));
        assertEquals("/to/here.png", mapper.map("/from/here.png"));
        assertEquals("/to/from/until/when.txt", mapper.map("/from/from/until/when.txt"));
    }

}
