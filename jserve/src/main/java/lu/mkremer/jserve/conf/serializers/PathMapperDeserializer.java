package lu.mkremer.jserve.conf.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.api.mapper.PathMapper;

import java.io.IOException;

public class PathMapperDeserializer extends JsonDeserializer<PathMapper>{

	private final PathMapperFactory factory;

	public PathMapperDeserializer(PathMapperFactory factory) {
		this.factory = factory;
	}

	@Override
	public PathMapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		return factory.parseMapper(node);
	}

}
