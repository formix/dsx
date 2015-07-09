/****************************************************************************
 * Copyright 2009-2014 Jean-Philippe Gravel, P. Eng. CSDP
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

import org.apache.commons.lang.StringEscapeUtils;

public class XmlText implements XmlContent {

	private long id;
	private StringBuilder text;

	public XmlText(String text) {
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

	public String getText() {
		return this.text.toString();
	}

	public void setText(String value) {
		this.text = new StringBuilder(value);
	}

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
		return this.text.toString().equals(other.text.toString());
	}

}
