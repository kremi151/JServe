package lu.mkremer.jserve.mappers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMapper implements PathMapper {

    public static final String JSON_TYPE = "regex";

    private Pattern pattern;
    private String destination;
    private boolean terminal;

    public RegexMapper(Pattern pattern, String destination, boolean terminal) {
        this.pattern = pattern;
        this.destination = destination;
        this.terminal = terminal;
    }

    public RegexMapper() {
        this(null, "", false);
    }

    @Override
    public MapperState applies(String path) {
        if (pattern == null || !pattern.matcher(path).find()) {
            return MapperState.NOT_APPLICABLE;
        }
        if (terminal) {
            return MapperState.ACCEPT_FINISH;
        }
        return MapperState.ACCEPT_FORWARD;
    }

    @Override
    public String map(String path) {
        if (pattern == null) {
            return path;
        }
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return path;
        }
        String result = destination.replaceAll("\\$0", matcher.group(0));
        int groupCount = matcher.groupCount();
        for (int i = 1 ; i <= groupCount ; i++) {
            result = result.replaceAll("\\$" + i, matcher.group(i));
        }
        return result;
    }

    @Override
    public String getType() {
        return JSON_TYPE;
    }

    public String getPattern() {
        return pattern == null ? "" : pattern.toString();
    }

    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
}
