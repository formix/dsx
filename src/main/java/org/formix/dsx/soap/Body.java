package org.formix.dsx.soap;

import org.formix.dsx.builders.XmlContentTypeName;
import org.formix.dsx.builders.XmlNameSpace;

/**
 * The body of an SOAP envelope.
 * 
 * @author jpgravel
 *
 * @param <T>
 *            The content type.
 */
@XmlNameSpace("http://schemas.xmlsoap.org/soap/envelope/")
public class Body<T extends Object> {

	private T content;

	/**
	 * Instanciate a SOAP body instance.
	 */
	public Body() {
		this.content = null;
	}

	/**
	 * Instanciate a body with the given content.
	 * 
	 * @param content
	 *            The content to use in the body.
	 */
	public Body(T content) {
		this.content = content;
	}

	/**
	 * Gets the content of the body. Upon serialization, the corresponding node
	 * will be named according to the type name of the actual content.
	 * 
	 * @return the content of the body.
	 */
	@XmlContentTypeName
	public T getContent() {
		return content;
	}

	/**
	 * Sets the content of the body.
	 * 
	 * @param content
	 *            The content of the body.
	 */
	@XmlContentTypeName
	public void setContent(T content) {
		this.content = content;
	}

}
