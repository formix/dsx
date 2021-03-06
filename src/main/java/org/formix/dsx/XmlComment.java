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

/**
 * Defines a comment xml chunk.
 * 
 * @author jpgravel
 *
 */
public class XmlComment extends XmlText {

	/**
	 * Creates an XmlComment section using the given text.
	 * 
	 * @param text
	 *            The text to put in the comment.
	 */
	public XmlComment(String text) {
		super(text);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append(this.toXml());
	}

	@Override
	public String toXml() {
		return "<!--" + this.getText() + "-->";
	}
}
