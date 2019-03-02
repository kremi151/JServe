package lu.mkremer.jserve.util;

import com.fasterxml.jackson.databind.JsonNode;

public class ValueHelper {

    public static Object getDefaultValueForType(Class<?> typeClazz) {
        if (typeClazz == char.class) {
            return '\0';
        } else if (typeClazz == boolean.class) {
            return false;
        } else if (typeClazz == byte.class) {
            return (byte) 0;
        } else if (typeClazz == short.class) {
            return (short) 0;
        } else if (typeClazz == int.class) {
            return 0;
        } else if (typeClazz == long.class) {
            return 0L;
        } else if (typeClazz == float.class) {
            return 0.0F;
        } else if (typeClazz == double.class) {
            return 0.0D;
        } else if (!typeClazz.isPrimitive()) {
            return null;
        }
        throw new IllegalArgumentException("Cannot determine default value of type " + typeClazz);
    }

    public static Object parseValueForType(String value, Class<?> typeClazz) {
        if (value == null || value.length() == 0) {
            return getDefaultValueForType(typeClazz);
        }
        if (typeClazz == String.class) {
            return value;
        } else if (typeClazz == Character.class || typeClazz == char.class) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("String with length " + value.length() + " cannot be transformed into a character");
            }
            return value.charAt(0);
        } else if (typeClazz == Boolean.class || typeClazz == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (typeClazz == Byte.class || typeClazz == byte.class) {
            return Byte.parseByte(value);
        } else if (typeClazz == Short.class || typeClazz == short.class) {
            return Short.parseShort(value);
        } else if (typeClazz == Integer.class || typeClazz == int.class) {
            return Integer.parseInt(value);
        } else if (typeClazz == Long.class || typeClazz == long.class) {
            return Long.parseLong(value);
        } else if (typeClazz == Float.class || typeClazz == float.class) {
            return Float.parseFloat(value);
        } else if (typeClazz == Double.class || typeClazz == double.class) {
            return Double.parseDouble(value);
        }
        throw new IllegalArgumentException("Cannot handle value of type " + typeClazz);
    }

    public static Object parseJsonValueForType(JsonNode json, Class<?> typeClazz) {
        if (typeClazz == String.class) {
            return json.asText();
        } else if (typeClazz == Character.class || typeClazz == char.class) {
            String text = json.asText();
            if (text.length() != 1) {
                throw new IllegalArgumentException("String with length " + text.length() + " cannot be transformed into a character");
            }
            return text.charAt(0);
        } else if (typeClazz == Boolean.class || typeClazz == boolean.class) {
            return json.asBoolean();
        } else if (typeClazz == Byte.class || typeClazz == byte.class) {
            return (byte) json.asInt();
        } else if (typeClazz == Short.class || typeClazz == short.class) {
            return (short) json.asInt();
        } else if (typeClazz == Integer.class || typeClazz == int.class) {
            return json.asInt();
        } else if (typeClazz == Long.class || typeClazz == long.class) {
            return json.asLong();
        } else if (typeClazz == Float.class || typeClazz == float.class) {
            return (float) json.asDouble();
        } else if (typeClazz == Double.class || typeClazz == double.class) {
            return json.asDouble();
        }
        throw new IllegalArgumentException("Cannot handle value of type " + typeClazz);
    }

}
