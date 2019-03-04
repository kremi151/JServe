package lu.mkremer.jserve.io;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WritableJsonGeneratorNode implements WritableNode {

    private final JsonGenerator gen;

    public WritableJsonGeneratorNode(JsonGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void writeStringProperty(String name, String value) throws IOException {
        gen.writeStringField(name, value);
    }

    @Override
    public void writeObjectProperty(String name, Object value) throws IOException {
        gen.writeObjectField(name, value);
    }

    @Override
    public void startObject(String name) throws IOException {
        gen.writeObjectFieldStart(name);
    }

    @Override
    public void endObject() throws IOException {
        gen.writeEndObject();
    }
}
