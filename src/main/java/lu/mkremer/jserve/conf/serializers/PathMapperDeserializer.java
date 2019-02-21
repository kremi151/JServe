package lu.mkremer.jserve.conf.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import lu.mkremer.jserve.mappers.*;

public class PathMapperDeserializer extends JsonDeserializer<PathMapper>{

	@Override
	public PathMapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		final String type = node.get("type").asText("unknown");
		if (type.equalsIgnoreCase("index")) {
			return new IndexPathMapper(node.get("file").asText("index.html"));
		} else if (type.equalsIgnoreCase("prefix")) {
			PrefixPathMapper mapper = new PrefixPathMapper();
			mapper.setFromPrefix(node.get("from").asText("/"));
			mapper.setToPrefix(node.get("to").asText("/"));
			mapper.setIgnoreCase(node.get("ignore_case").asBoolean(false));
			mapper.setTerminal(node.get("terminal").asBoolean(false));
			return mapper;
		} else if (type.equalsIgnoreCase("glob")) {
			GlobMapper mapper = new GlobMapper();
			mapper.setGlobMatcher(node.get("pattern").asText("/**"));
			mapper.setDestination(node.get("destination").asText("/"));
			mapper.setTerminal(node.get("terminal").asBoolean(false));
			return mapper;
		} else {
			return new UnknownPathMapper();
		}
	}

}
