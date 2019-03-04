package lu.mkremer.jserve.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Child {

    /**
     * Whether the child field is mandatory
     */
    boolean required() default false;

    /**
     * The property name to be used. If left blank, the field's name will be used.
     * If a list is specified, the first property name to be found in the configuration will be used during
     * deserialization.
     * For serialization, the first name of the list will always be used.
     */
    String[] name() default "";

}
