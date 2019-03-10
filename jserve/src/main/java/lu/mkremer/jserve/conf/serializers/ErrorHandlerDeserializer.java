package lu.mkremer.jserve.conf.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lu.mkremer.jserve.errorhandling.ErrorHandler;
import lu.mkremer.jserve.errorhandling.ErrorPageHandler;
import lu.mkremer.jserve.errorhandling.RedirectErrorHandler;
import lu.mkremer.jserve.errorhandling.UnknownErrorHandler;

import java.io.IOException;

public class ErrorHandlerDeserializer extends JsonDeserializer<ErrorHandler> {

	@Override
	public ErrorHandler deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		final String type = node.get("type").asText("unknown");
		if (type.equalsIgnoreCase("page")) {
			ErrorPageHandler handler = new ErrorPageHandler();
			handler.setFrom(node.get("from").asInt(0));
			handler.setTo(node.get("to").asInt(999));
			handler.setPath(node.get("path").asText("/error.html"));
			return handler;
		} else if (type.equalsIgnoreCase("redirect")) {
			RedirectErrorHandler handler = new RedirectErrorHandler();
			handler.setFrom(node.get("from").asInt(0));
			handler.setTo(node.get("to").asInt(999));
			handler.setRedirect(node.get("path").asText("/error.html"));
			return handler;
		} else {
			return new UnknownErrorHandler();
		}
	}

}
