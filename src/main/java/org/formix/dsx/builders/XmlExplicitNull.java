package org.formix.dsx.builders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The default XmlBuilder behavior is to skip object property. Adding the
 * XmlExplicitNull annotation to a property will force node addition with the
 * attribute 'ns:nil="true"'.
 * 
 * @author jpgravel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XmlExplicitNull {

}
