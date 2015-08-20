package org.formix.dsx.soap;

import org.formix.dsx.builders.XmlIgnore;
import org.formix.dsx.builders.XmlNameSpace;
import org.formix.dsx.builders.XmlOrder;

/**
 * Defines a SOAP Envelope.
 * 
 * @author jpgravel
 *
 * @param <T>
 *            The type of the body content.
 */
@XmlNameSpace("http://schemas.xmlsoap.org/soap/envelope/")
public class Envelope<T extends Object> {

	private Header header;
	private Body<T> body;

	/**
	 * Instanciate an envelope instance.
	 */
	public Envelope() {
		this.header = new Header() {
		};
		this.body = null;
	}

	/**
	 * Gets the header of the SOAP message.
	 * 
	 * @return the header of the SOAP message.
	 */
	@XmlOrder(0)
	public Header getHeader() {
		return header;
	}

	/**
	 * Sets the header of the SOAP message.
	 * 
	 * @param header
	 *            the header of the SOAP message.
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * Gets the body of the SOAP message.
	 * 
	 * @return the
	 */
	@XmlOrder(1)
	public Body<T> getBody() {
		return body;
	}

	/**
	 * Sets the body of the SOAP message.
	 * 
	 * @param body
	 *            the body of the SOAP message.
	 */
	public void setBody(Body<T> body) {
		this.body = body;
	}

	/**
	 * Sets the content of the body. This property is ignored during
	 * serialization with the XmlBuilder.
	 * 
	 * @param content
	 *            the content of the body.
	 */
	@XmlIgnore
	public void setBodyContent(T content) {
		if (this.body == null) {
			this.body = new Body<T>();
		}
		this.body.setContent(content);
	}

	/**
	 * Gets the content of the body.
	 * 
	 * @return the content of the body
	 */
	@XmlIgnore
	public T getBodyContent() {
		return this.body.getContent();
	}

}
