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

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

class XmlHandler extends DefaultHandler2 {

	private XmlContentListener listener;
	private XmlElement rootElement;
	private Stack<XmlElement> elementStack;
	private String currentCDATA;

	public XmlHandler() {
		this(new XmlContentAdapter());
	}

	public XmlHandler(XmlContentListener listener) {
		this.listener = listener;
		this.rootElement = null;
		this.elementStack = new Stack<XmlElement>();
		this.currentCDATA = null;
	}

	public XmlElement getRootElement() {
		return rootElement;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		XmlElement newElement = new XmlElement(qName);
		for (int i = 0; i < attributes.getLength(); i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			newElement.setAttribute(name, value);
		}
		this.elementStack.add(newElement);
		this.listener.elementCreated(newElement);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		XmlElement child = this.elementStack.pop();
		this.mergeTextChilds(child);
		if (this.elementStack.size() > 0)
			this.elementStack.peek().addChild(child);
		else
			this.rootElement = child;
	}

	private void mergeTextChilds(XmlElement elem) {
		for (int i = 0; i < (elem.getChilds().size() - 1); i++) {
			XmlContent c1 = elem.getChild(i);
			XmlContent c2 = elem.getChild(i + 1);
			if (this.mergeText(c1, c2)) {
				elem.getChilds().remove(i + 1); // remove c2
				i--;
			}
		}
	}

	private boolean mergeText(XmlContent c1, XmlContent c2) {
		if ((c1 instanceof XmlText) && (c2 instanceof XmlText)
				&& (c1.getClass().equals(c2.getClass()))) {
			XmlText text2 = (XmlText) c2;
			((XmlText) c1).append(text2.getText());
			return true;
		}
		return false;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		// TODO we should handle the case where more than 16KB is received and
		// the Text element is splitted in many XmlText
		// We found out that case when receiving base64 files
		// 43,224 bytes were split in 3 XmlText

		String str = new String(ch, start, length);
		if (this.currentCDATA == null) {
			XmlText text = this.elementStack.peek().addText(str);
			this.listener.textCreated(text);
		} else {
			this.currentCDATA += new String(str);
		}
	}

	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		String comment = new String(ch, start, length);
		if (!this.elementStack.isEmpty()) {
			XmlComment cmt = this.elementStack.peek().addComment(comment);
			this.listener.commentCreated(cmt);
		}
	}

	@Override
	public void startCDATA() throws SAXException {
		this.currentCDATA = "";
	}

	@Override
	public void endCDATA() throws SAXException {
		XmlCDATA cdata = this.elementStack.peek().addCDATA(this.currentCDATA);
		this.listener.cdataCreated(cdata);
		this.currentCDATA = null;
	}

	public XmlElement peekTopElement() {
		if (this.elementStack.size() == 0) {
			return null;
		}
		return this.elementStack.peek();
	}
}
