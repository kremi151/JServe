package lu.mkremer.jserve.conf.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.io.WritableJsonGeneratorNode;
import lu.mkremer.jserve.api.mapper.PathMapper;

import java.io.IOException;

public class PathMapperSerializer extends JsonSerializer<PathMapper>{

	private final PathMapperFactory factory;

	public PathMapperSerializer(PathMapperFactory factory) {
		this.factory = factory;
	}

	@Override
	public void serialize(PathMapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		this.factory.serializeMapper(new WritableJsonGeneratorNode(gen), value);
		gen.writeEndObject();
	}

}
