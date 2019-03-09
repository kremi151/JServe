package lu.mkremer.jserve.api.util;

import lu.mkremer.jserve.exception.NotConstructableException;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class DefaultConstructorFactory<T> implements Supplier<T> {

    private final Class<T> clazz;

    public DefaultConstructorFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T get() {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new NotConstructableException("No default constructor found", e);
        } catch (Exception e) {
            throw new NotConstructableException("Default constructor could not be invoked", e);
        }
    }
}
