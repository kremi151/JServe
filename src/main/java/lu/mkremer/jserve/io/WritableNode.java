package lu.mkremer.jserve.io;

import java.io.IOException;

public interface WritableNode {

    void writeStringProperty(String name, String value) throws IOException;
    void writeObjectProperty(String name, Object value) throws IOException;

}
