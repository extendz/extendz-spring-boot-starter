package club.extendz.spring.modelMeta.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import club.extendz.spring.modelMeta.annotations.enums.InputType;

@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Extendz {

	boolean title() default false;

	boolean subTitle() default false;

	InputType type() default InputType.NONE;

}
