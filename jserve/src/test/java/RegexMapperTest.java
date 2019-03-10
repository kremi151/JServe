import lu.mkremer.jserve.api.mapper.state.MapperState;
import lu.mkremer.jserve.mappers.RegexMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegexMapperTest {

    @Tag("fast")
    @Test
    public void testRegexPatternNonTerminal() {
        testRegexPattern(false, MapperState.ACCEPT_FORWARD);
    }

    @Tag("fast")
    @Test
    public void testRegexPatternTerminal() {
        testRegexPattern(true, MapperState.ACCEPT_FINISH);
    }

    private void testRegexPattern(boolean terminal, MapperState expectedAcceptedState) {
        RegexMapper mapper = new RegexMapper();
        mapper.setPattern(".*\\/path-([0-9]+).*\\/(\\S+)\\.(\\S+)");
        mapper.setTo("/dest/$2-$1.$3");
        mapper.setTerminal(terminal);

        assertEquals(expectedAcceptedState, mapper.applies("/some/path-007/sub/file.txt"));
        assertEquals(MapperState.NOT_APPLICABLE, mapper.applies("/some/other/non-matching/path.txt"));
        assertEquals("/dest/file-007.txt", mapper.map("/some/path-007/sub/file.txt"));
    }
}
