package lu.mkremer.jserve.mappers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		use = JsonTypeInfo.Id.NAME,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = GlobMapper.class, name = GlobMapper.JSON_TYPE),
		@JsonSubTypes.Type(value = IndexPathMapper.class, name = IndexPathMapper.JSON_TYPE),
		@JsonSubTypes.Type(value = PrefixPathMapper.class, name = PrefixPathMapper.JSON_TYPE),
		@JsonSubTypes.Type(value = ChainMapper.class, name = ChainMapper.JSON_TYPE),
		@JsonSubTypes.Type(value = FirstMatchingMapper.class, name = FirstMatchingMapper.JSON_TYPE),
		@JsonSubTypes.Type(value = RegexMapper.class, name = RegexMapper.JSON_TYPE)
})
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

	String getType();
	
}
