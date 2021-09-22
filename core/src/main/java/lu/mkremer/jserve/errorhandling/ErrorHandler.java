package lu.mkremer.jserve.errorhandling;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.io.WriteableOutputStream;

@JsonTypeInfo(
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		use = JsonTypeInfo.Id.NAME,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = DefaultErrorHandler.class, name = DefaultErrorHandler.JSON_TYPE),
		@JsonSubTypes.Type(value = ErrorPageHandler.class, name = ErrorPageHandler.JSON_TYPE),
		@JsonSubTypes.Type(value = RedirectErrorHandler.class, name = RedirectErrorHandler.JSON_TYPE)
})
public interface ErrorHandler {
	
	boolean canHandle(int errorCode, String path);
	
	void respond(int errorCode, String status, String path, WriteableOutputStream out, ServerConfiguration configuration) throws IOException;

	String getType();

}
