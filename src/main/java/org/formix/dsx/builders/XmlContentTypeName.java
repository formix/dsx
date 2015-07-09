package org.formix.dsx.builders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Tells XmlBuilder to use the type name instead of the method name for the
 * XmlElement.
 * 
 * @author jpgravel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XmlContentTypeName {

}
