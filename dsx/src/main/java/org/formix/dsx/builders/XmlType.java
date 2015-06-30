package org.formix.dsx.builders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Tells the XmlBuilder to add the contained object type as an attribute to the
 * corresponding Xmlelement. This attribute shall be set on all undefined
 * references (Abstract classes, interfaces and Object class references).
 * 
 * 
 * *** delete this annotation and add the XmlBuilder following behavior: if the
 * method return type is different than the returned type, add the type
 * information to the method element ***
 * 
 * @author jpgravel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XmlType {

	/**
	 * @return the namespace for primitive data types see {@link http
	 *         ://www.w3.org
	 *         /TR/2012/REC-xmlschema11-2-20120405/datatypes.html#built
	 *         -in-primitive-datatypes}
	 * 
	 */
	String valueNameSpace() default "http://www.w3.org/2001/XMLSchema";

	/**
	 * @return where the "type" attribute is defined.
	 */
	String attributeNameSpace() default "http://www.w3.org/2001/XMLSchema-instance";

}
