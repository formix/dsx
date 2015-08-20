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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;
import org.junit.Assert;
import org.junit.Test;

public class TestXmlElement {

	@Test
	public void testXHTML() {
		System.out.println(createElement());
	}

	private XmlElement createElement() {
		XmlElement html = new XmlElement("html");
		html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
		XmlElement head = html.addElement("head");

		head.addElement("title").addText("This is an XHTML example");

		XmlElement script = head.addElement("script");
		script.setAttribute("type", "text/javascript");
		script.addText("//<!--");
		script.addCDATA("\nfunction f() { alert(\"hello world!\"); }\n//");
		script.addText("-->");

		html.addComment("this is a comment");

		XmlElement body = html.addElement("body");
		body.setAttribute("onload", "f()");
		body.addElement("h1").addText("Lorem ipsum dolor sit amet");

		body.addElement("p")
				.addText(
						"Lorem ipsum dolor sit amet, consectetur adipisicing "
								+ "elit, sed do eiusmod tempor incididunt ut labore et "
								+ "dolore magna aliqua. Ut enim ad minim veniam, quis "
								+ "nostrud exercitation ullamco laboris nisi ut aliquip "
								+ "ex ea commodo consequat. Duis aute irure dolor in "
								+ "reprehenderit in voluptate velit esse cillum dolore "
								+ "eu fugiat nulla pariatur. Excepteur sint occaecat "
								+ "cupidatat non proident, sunt in culpa qui officia "
								+ "deserunt mollit anim id est laborum.");

		body.addElement("hr").setAttribute("class", "myRuler");

		body.addElement("h1").addText("Duis aute irure dolor");

		body.addElement("p")
				.addText(
						"Duis aute irure dolor in reprehenderit in voluptate "
								+ "velit esse cillum dolore eu fugiat nulla pariatur. "
								+ "Excepteur sint occaecat cupidatat non proident, sunt "
								+ "in culpa qui officia deserunt mollit anim id est "
								+ "laborum. Lorem ipsum dolor sit amet, consectetur "
								+ "adipisicing elit, sed do eiusmod tempor incididunt ut "
								+ "labore et dolore magna aliqua. Ut enim ad minim "
								+ "veniam, quis nostrud exercitation ullamco laboris nisi "
								+ "ut aliquip ex ea commodo consequat.");

		body.addElement("hr");

		return html;
	}

	@Test
	public void testDocumentReader() throws Exception {
		String expected = createElement().toString();
		XmlElement elem = XmlElement.readXML(expected);
		System.out.println(elem);
		Assert.assertEquals(expected, elem.toString());
	}

	@Test
	public void testReaderHugeDoc() throws Exception {
		System.setProperty("entityExpansionLimit", "5000000");
		XmlElement elem = XmlElement.readXML(new File("hugedoc.xhtml"));
		System.out.println("Nombre d'éléments au total: "
				+ this.countChildElements(elem));
	}

	private int countChildElements(XmlContent content) {
		if (!(content instanceof XmlElement))
			return 0;
		XmlElement elem = (XmlElement) content;
		int count = elem.getChilds().size();
		for (XmlContent c : elem.getChilds())
			count += this.countChildElements(c);
		return count;
	}

	@Test
	public void testWithByteArray() throws Exception {
		String xmlSource = this.readToEnd("FileData.xml");
		XmlElement elem = XmlElement.readXML(xmlSource);
		Assert.assertEquals(xmlSource, elem.toString());
	}

	private String readToEnd(String resName) throws Exception {
		InputStreamReader in = new InputStreamReader(this.getClass()
				.getResourceAsStream(resName));
		String str = "";
		BufferedReader reader = new BufferedReader(in);
		String line = reader.readLine();
		while (line != null) {
			str += line;
			line = reader.readLine();
		}
		reader.close();
		in.close();
		return str;
	}
}
