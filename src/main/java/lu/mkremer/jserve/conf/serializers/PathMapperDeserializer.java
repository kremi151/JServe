package lu.mkremer.jserve.conf.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.mappers.*;

public class PathMapperDeserializer extends JsonDeserializer<PathMapper>{

	@Override
	public PathMapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		return PathMapperFactory.get().parseMapper(node);
	}

}
