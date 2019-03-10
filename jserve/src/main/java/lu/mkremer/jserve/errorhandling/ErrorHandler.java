package lu.mkremer.jserve.errorhandling;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.mkremer.jserve.conf.ServerConfiguration;
import lu.mkremer.jserve.conf.serializers.ErrorHandlerDeserializer;
import lu.mkremer.jserve.conf.serializers.ErrorHandlerSerializer;
import lu.mkremer.jserve.io.WriteableOutputStream;

import java.io.IOException;

@JsonSerialize(using = ErrorHandlerSerializer.class)
@JsonDeserialize(using = ErrorHandlerDeserializer.class)
public interface ErrorHandler {
	
	boolean canHandle(int errorCode, String path);
	
	void respond(int errorCode, String status, String path, WriteableOutputStream out, ServerConfiguration configuration) throws IOException;

}
