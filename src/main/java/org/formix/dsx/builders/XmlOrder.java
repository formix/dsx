package org.formix.dsx.builders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells in which order the corresponding XML element shall appear in the output
 * XML document.
 * 
 * Ordered elements takes precedence on unordered elements. Two unordered
 * elements are compared based on alphabetical ascending order. Two ordered
 * elements are sorted by ascending order.
 * 
 * @author jpgravel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XmlOrder {
	int value() default 0;
}
