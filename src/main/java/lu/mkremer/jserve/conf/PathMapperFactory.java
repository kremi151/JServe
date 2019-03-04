package lu.mkremer.jserve.conf;

import com.fasterxml.jackson.databind.JsonNode;
import lu.mkremer.jserve.api.annotation.Child;
import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;
import lu.mkremer.jserve.exception.DuplicateEntryException;
import lu.mkremer.jserve.exception.InvalidConfigurableException;
import lu.mkremer.jserve.exception.NotMappableException;
import lu.mkremer.jserve.exception.UnknownIdException;
import lu.mkremer.jserve.io.WritableNode;
import lu.mkremer.jserve.mappers.PathMapper;
import lu.mkremer.jserve.util.DefaultConstructorFactory;
import lu.mkremer.jserve.util.StringHelper;
import lu.mkremer.jserve.util.ValueHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Supplier;

public class PathMapperFactory {

    private static PathMapperFactory instance = null;

    private final HashMap<String, Class<? extends PathMapper>> ID_TO_CLASS = new HashMap<>();
    private final HashMap<Class<? extends PathMapper>, String> CLASS_TO_ID = new HashMap<>();
    private final HashMap<String, Supplier<? extends PathMapper>> ID_TO_FACTORY = new HashMap<>();

    private PathMapperFactory() {}

    public synchronized <M extends PathMapper> void registerPathMapper(Class<M> clazz, Supplier<M> factory) {
        Configurable configurable = clazz.getAnnotation(Configurable.class);
        if (configurable == null) {
            throw new InvalidConfigurableException("Missing Configurable annotation on class " + clazz);
        }
        final String id = configurable.id().toLowerCase();
        if (ID_TO_CLASS.containsKey(id)) {
            throw new DuplicateEntryException("Duplicate entry for id " + configurable.id());
        }
        if (CLASS_TO_ID.containsKey(clazz)) {
            throw new DuplicateEntryException("Duplicate entry for class " + clazz);
        }
        ID_TO_CLASS.put(id, clazz);
        CLASS_TO_ID.put(clazz, id);
        ID_TO_FACTORY.put(id, factory);
    }

    public synchronized <M extends PathMapper> void registerPathMapper(Class<M> clazz) {
        registerPathMapper(clazz, new DefaultConstructorFactory<>(clazz));
    }

    public synchronized void reset() {
        ID_TO_CLASS.clear();
        CLASS_TO_ID.clear();
        ID_TO_FACTORY.clear();
    }

    public <M extends PathMapper> M parseMapper(JsonNode node) {
        if (!node.has("type")) {
            throw new UnknownIdException("No \"type\" property defined");
        }
        final String type = node.get("type").asText("unknown").toLowerCase();
        Class<? extends PathMapper> clazz = ID_TO_CLASS.get(type);
        if (clazz == null) {
            throw new NotMappableException("Unknown type: " + type);
        }
        Supplier<? extends PathMapper> factory = ID_TO_FACTORY.get(type);
        if (factory == null) {
            throw new NotMappableException("No factory registered, this should not happen");
        }
        PathMapper mapper = factory.get();

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ConfigField prop = field.getAnnotation(ConfigField.class);
                if (prop != null) {
                    deserializeConfigField(node, prop, field, clazz, mapper);
                    continue;
                }
                Child childProp = field.getAnnotation(Child.class);
                if (childProp != null) {
                    deserializeChildField(node, childProp, field, clazz, mapper);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NotMappableException("Reflection error", e);
        }

        return (M) mapper;
    }

    private void deserializeConfigField(JsonNode node, ConfigField prop, Field field, Class<? extends PathMapper> clazz, PathMapper mapper) throws InvocationTargetException, IllegalAccessException {
        String[] nameArray = prop.name();
        if (nameArray.length == 0 || (nameArray.length == 1 && nameArray[0].length() == 0)) {
            nameArray = new String[] {StringHelper.toSnakeCase(field.getName())};
        }
        JsonNode valueNode = readJsonValue(node, nameArray);
        Object value;
        if (valueNode == null && prop.required()) {
            throw new NotMappableException("Missing property: " + nameArray[0]);
        }
        if (valueNode == null) {
            value = ValueHelper.parseValueForType(prop.defaultValue(), field.getType());
        } else {
            value = ValueHelper.parseJsonValueForType(valueNode, field.getType());
        }
        final String setterName = StringHelper.toSetterName(field.getName());
        Method setter = tryGetMethod(clazz, setterName, field.getType());
        if (setter != null) {
            setter.setAccessible(true);
            setter.invoke(mapper, value);
        } else {
            field.set(mapper, value);
        }
    }

    private void deserializeChildField(JsonNode node, Child prop, Field field, Class<? extends PathMapper> clazz, PathMapper mapper) throws InvocationTargetException, IllegalAccessException {
        String[] nameArray = prop.name();
        if (nameArray.length == 0 || (nameArray.length == 1 && nameArray[0].length() == 0)) {
            nameArray = new String[] {StringHelper.toSnakeCase(field.getName())};
        }
        JsonNode valueNode = readJsonValue(node, nameArray);
        if (valueNode == null) {
            if (prop.required()) {
                throw new NotMappableException("Required child mapper " + nameArray[0] + " not found");
            } else {
                return;
            }
        } else if (!valueNode.isObject()) {
            throw new NotMappableException("Child mapper " + nameArray[0] + " is not a path mapper");
        }
        PathMapper child = parseMapper(valueNode);
        final String setterName = StringHelper.toSetterName(field.getName());
        Method setter = tryGetMethod(clazz, setterName, field.getType());
        if (setter != null) {
            setter.setAccessible(true);
            setter.invoke(mapper, child);
        } else {
            field.set(mapper, child);
        }
    }

    public void serializeMapper(WritableNode out, PathMapper mapper) throws IOException {
        final String type = CLASS_TO_ID.get(mapper.getClass());
        if (type == null) {
            throw new NotMappableException("PathMapper class " + mapper.getClass() + " is not registered");
        }
        out.writeStringProperty("type", type);
        try {
            Field[] fields = mapper.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ConfigField prop = field.getAnnotation(ConfigField.class);
                if (prop != null) {
                    serializeConfigField(prop, field, mapper, out);
                    continue;
                }
                Child childProp = field.getAnnotation(Child.class);
                if (childProp != null) {
                    serializeChildField(childProp, field, mapper, out);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NotMappableException("Reflection error", e);
        }
    }

    private void serializeConfigField(ConfigField prop, Field field, PathMapper mapper, WritableNode out) throws InvocationTargetException, IllegalAccessException, IOException {
        String[] nameArray = prop.name();
        if (nameArray.length == 0 || (nameArray.length == 1 && nameArray[0].length() == 0)) {
            nameArray = new String[] {StringHelper.toSnakeCase(field.getName())};
        }
        final String getterName = StringHelper.toGetterName(field.getName(), field.getType());
        Object value;
        Method getter = tryGetMethod(mapper.getClass(), getterName, field.getType());
        if (getter != null) {
            getter.setAccessible(true);
            value = getter.invoke(mapper);
        } else {
            value = field.get(mapper);
        }
        if (value == null) {
            value = ValueHelper.parseValueForType(prop.defaultValue(), field.getType());
        }
        out.writeObjectProperty(nameArray[0], value);
    }

    private void serializeChildField(Child prop, Field field, PathMapper mapper, WritableNode out) throws InvocationTargetException, IllegalAccessException, IOException {
        String[] nameArray = prop.name();
        if (nameArray.length == 0 || (nameArray.length == 1 && nameArray[0].length() == 0)) {
            nameArray = new String[] {StringHelper.toSnakeCase(field.getName())};
        }
        final String getterName = StringHelper.toGetterName(field.getName(), field.getType());
        Method getter = tryGetMethod(mapper.getClass(), getterName, field.getType());
        PathMapper child;
        if (getter != null) {
            getter.setAccessible(true);
            child = (PathMapper) getter.invoke(mapper);
        } else {
            child = (PathMapper) field.get(mapper);
        }
        if (child == null && prop.required()) {
            throw new NotMappableException("Required child mapper " + nameArray[0] + " is not set");
        }
        if (child != null) {
            out.startObject(nameArray[0]);
            serializeMapper(out, child);
            out.endObject();
        }
    }

    private JsonNode readJsonValue(JsonNode json, String[] names) {
        for (String name : names) {
            if (!json.has(name)) {
                continue;
            }
            return json.get(name);
        }
        return null;
    }

    private Method tryGetMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static synchronized PathMapperFactory get() {
        if (instance == null) {
            instance = new PathMapperFactory();
        }
        return instance;
    }
}
