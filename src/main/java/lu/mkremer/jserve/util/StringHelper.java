package lu.mkremer.jserve.util;

public class StringHelper {

    public static String toSnakeCase(String in) {
        String result = in
                .replaceAll("([A-Z]+(?=[A-Z])|[A-Z])", "_$1")
                .toLowerCase();
        if (result.startsWith("_")) {
            return result.substring(1);
        } else {
            return result;
        }
    }

    public static String toGetterName(String name, Class<?> type) {
        return toGetterName(name, type == Boolean.class || type == boolean.class);
    }

    public static String toGetterName(String name, boolean isBool) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Empty name");
        }
        if (isBool) {
            if (name.length() == 1) {
                return "is" + name.toUpperCase();
            } else {
                return String.format("is%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
            }
        } else {
            if (name.length() == 1) {
                return "get" + name.toUpperCase();
            } else {
                return String.format("get%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
            }
        }
    }

    public static String toSetterName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Empty name");
        }
        if (name.length() == 1) {
            return "set" + name.toUpperCase();
        } else {
            return String.format("set%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
        }
    }
}
