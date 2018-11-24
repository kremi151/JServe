package lu.mkremer.jserve.mappers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lu.mkremer.jserve.conf.serializers.PathMapperDeserializer;
import lu.mkremer.jserve.conf.serializers.PathMapperSerializer;

@JsonSerialize(using = PathMapperSerializer.class)
@JsonDeserialize(using = PathMapperDeserializer.class)
public interface PathMapper {

	/**
	 * Determines whether this mapper applies to the given path
	 * @param path
	 * @return
	 */
	MapperState applies(String path);
	
	/**
	 * Maps the given path to the one on the local file system
	 * @param path
	 * @return
	 */
	String map(String path);
	
}
