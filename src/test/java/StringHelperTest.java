import lu.mkremer.jserve.util.StringHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringHelperTest {

    @Tag("fast")
    @Test
    public void snakeCaseTest() {
        assertEquals("this_is_a_test", StringHelper.toSnakeCase("ThisIsATest"));
        assertEquals("this_is_ab_test", StringHelper.toSnakeCase("ThisIsABTest"));
        assertEquals("this_is_abc_test", StringHelper.toSnakeCase("ThisIsABCTest"));
        assertEquals("this_is_abcd_test", StringHelper.toSnakeCase("ThisIsABCDTest"));
    }

}
