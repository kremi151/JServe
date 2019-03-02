package lu.mkremer.jserve.io;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class WritableObjectNode implements WritableNode {

    private final ObjectNode node;

    public WritableObjectNode(ObjectNode node) {
        this.node = node;
    }

    @Override
    public void writeStringProperty(String name, String value) {
        node.put(name, value);
    }

    @Override
    public void writeObjectProperty(String name, Object value) {
        node.putPOJO(name, value);
    }

}
