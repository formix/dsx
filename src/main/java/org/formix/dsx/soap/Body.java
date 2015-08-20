package org.formix.dsx.soap;

import org.formix.dsx.builders.XmlContentTypeName;
import org.formix.dsx.builders.XmlNameSpace;

@XmlNameSpace("http://schemas.xmlsoap.org/soap/envelope/")
public class Body<T extends Object> {

	private T content;

	public Body() {
		this.content = null;
	}

	public Body(T content) {
		this.content = content;
	}

	@XmlContentTypeName
	public T getContent() {
		return content;
	}

	@XmlContentTypeName
	public void setContent(T content) {
		this.content = content;
	}

}
