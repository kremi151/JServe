import lu.mkremer.jserve.io.CSVReader;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CSVReaderTest {

    @Tag("fast")
    @Test
    public void testCSVReader() throws IOException {
        String csv = "abc,def,ghi\njkl,mno,pqr\nstu,vwx,yza";
        CSVReader reader = new CSVReader(new StringReader(csv));
        assertArrayEquals(new String[] {"abc", "def", "ghi"}, reader.readRow());
        assertArrayEquals(new String[] {"jkl", "mno", "pqr"}, reader.readRow());
        assertArrayEquals(new String[] {"stu", "vwx", "yza"}, reader.readRow());
        assertNull(reader.readRow());
    }

}
