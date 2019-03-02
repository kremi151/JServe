package lu.mkremer.jserve.conf.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.io.WritableJsonGeneratorNode;
import lu.mkremer.jserve.mappers.PathMapper;

public class PathMapperSerializer extends JsonSerializer<PathMapper>{

	@Override
	public void serialize(PathMapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		PathMapperFactory.get().serializeMapper(new WritableJsonGeneratorNode(gen), value);
		gen.writeEndObject();
	}

}
