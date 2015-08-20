/****************************************************************************
 * Copyright 2009-2015 Jean-Philippe Gravel, P. Eng. CSDP
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.formix.dsx;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringEscapeUtils;
import org.formix.dsx.utils.SUID;

/**
 * Represents a text within the XML tree.
 * 
 * @author jpgravel
 *
 */
public class XmlText implements XmlContent {

	private long id;
	private StringBuilder text;

	/**
	 * Creates an empty XmlText instance.
	 */
	public XmlText() {
		this("");
	}

	/**
	 * Creates an XmlText instance with the given text.
	 * 
	 * @param text
	 *            The text used to initialize this XmlText instance.
	 */
	public XmlText(String text) {
		if (text == null) {
			throw new NullArgumentException("text");
		}
		this.text = new StringBuilder(text);
		this.id = -1;
	}

	@Override
	public long getId() {
		if (this.id == -1) {
			this.id = SUID.nextId();
		}
		return this.id;
	}

	/**
	 * Gets the text value.
	 * 
	 * @return the text value;
	 */
	public String getText() {
		return this.text.toString();
	}

	/**
	 * Sets the text value.
	 * 
	 * @param text
	 *            the text value.
	 */
	public void setText(String text) {
		this.text = new StringBuilder(text);
	}

	/**
	 * Appends a string to the current text.
	 * 
	 * @param value
	 *            the string to append.
	 */
	public void append(String value) {
		this.text.append(value);
	}

	@Override
	public String toXml() {
		return StringEscapeUtils.escapeXml(this.getText());
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append(this.toXml());
	}

	@Override
	public String toString() {
		return this.getText();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof XmlText))
			return false;
		XmlText other = (XmlText) o;
		return this.id == other.id && this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return this.getClass().getName().hashCode() ^ Long.hashCode(this.getId());
	}

}
