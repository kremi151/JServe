package lu.mkremer.jserve.conf.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lu.mkremer.jserve.mappers.GlobMapper;
import lu.mkremer.jserve.mappers.IndexPathMapper;
import lu.mkremer.jserve.mappers.PathMapper;
import lu.mkremer.jserve.mappers.PrefixPathMapper;

public class PathMapperSerializer extends JsonSerializer<PathMapper>{

	@Override
	public void serialize(PathMapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		if (value instanceof IndexPathMapper) {
			IndexPathMapper mapper = (IndexPathMapper) value;
			gen.writeStringField("type", "index");
			gen.writeStringField("file", mapper.getIndexFile());
		} else if (value instanceof PrefixPathMapper) {
			PrefixPathMapper mapper = (PrefixPathMapper) value;
			gen.writeStringField("type", "prefix");
			gen.writeStringField("from", mapper.getFromPrefix());
			gen.writeStringField("to", mapper.getToPrefix());
			gen.writeBooleanField("ignore_case", mapper.isIgnoreCase());
			gen.writeBooleanField("terminal", mapper.isTerminal());
		} else if (value instanceof GlobMapper) {
			GlobMapper mapper = (GlobMapper) value;
			gen.writeStringField("type", "glob");
			gen.writeStringField("pattern", mapper.getGlobMatcher());
			gen.writeStringField("destination", mapper.getDestination());
			gen.writeBooleanField("terminal", mapper.isTerminal());
		} else {
			gen.writeStringField("type", "unknown");
		}
		gen.writeEndObject();
	}

}
