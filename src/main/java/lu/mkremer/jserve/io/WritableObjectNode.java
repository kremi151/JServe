package lu.mkremer.jserve.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Stack;

public class WritableObjectNode implements WritableNode {

    private final ObjectMapper mapper;
    private final Stack<ObjectNode> stack = new Stack<>();

    public WritableObjectNode(ObjectNode node, ObjectMapper mapper) {
        stack.push(node);
        this.mapper = mapper;
    }

    @Override
    public synchronized void writeStringProperty(String name, String value) {
        stack.peek().put(name, value);
    }

    @Override
    public synchronized void writeObjectProperty(String name, Object value) {
        stack.peek().putPOJO(name, value);
    }

    @Override
    public synchronized void startObject(String name) {
        ObjectNode parent = stack.peek();
        ObjectNode child = mapper.createObjectNode();
        stack.push(child);
        parent.set(name, child);
    }

    @Override
    public synchronized void endObject() throws IOException {
        if (stack.size() <= 1) {
            throw new IOException("No parent node available");
        }
        stack.pop();
    }

}
