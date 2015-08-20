package org.formix.dsx.soap;

import org.formix.dsx.builders.XmlIgnore;
import org.formix.dsx.builders.XmlNameSpace;
import org.formix.dsx.builders.XmlOrder;


@XmlNameSpace("http://schemas.xmlsoap.org/soap/envelope/")
public class Envelope<T extends Object> {

	private Header header;
	private Body<T> body;
	
	public Envelope() {
		this.header = new Header();
		this.body = null;
	}

	@XmlOrder(0)
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	@XmlOrder(1)
	public Body<T> getBody() {
		return body;
	}
	
	public void setBody(Body<T> body) {
		this.body = body;
	}

	@XmlIgnore
	public void setBodyContent(T content) {
		if (this.body == null) {
			this.body = new Body<T>();
		}
		this.body.setContent(content);
	}
	
	@XmlIgnore
	public T getBodyContent() {
		return this.body.getContent();
	}

}
