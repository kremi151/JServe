package lu.mkremer.jserve.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a configurable property. During serialization, the field's getters and setters have precedence
 * over directly accessing the field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigField {

    /**
     * Whether the field is mandatory
     */
    boolean required() default false;

    /**
     * If optional, defines the default value to be applied if the property is not set
     */
    String defaultValue() default "";

    /**
     * The property name to be used. If left blank, the field's name will be used.
     * If a list is specified, the first property name to be found in the configuration will be used during
     * deserialization.
     * For serialization, the first name of the list will always be used.
     */
    String[] name() default "";

}
