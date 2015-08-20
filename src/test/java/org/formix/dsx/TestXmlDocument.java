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

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.formix.dsx.XmlDocumentType;
import org.formix.dsx.XmlDocument;
import org.formix.dsx.XmlElement;
import org.junit.Assert;
import org.junit.Test;

public class TestXmlDocument {

	private static final String EOL = System.getProperty("line.separator");

	@Test
	public void showEolChars() {
		System.out
				.println("\\r = " + (int) '\r' + "  " + "\\n = " + (int) '\n');
	}

	@Test
	public void testCleanUp() throws Exception {
		XmlElement elem = XmlElement.readXML("<test>" + EOL + "\t<name>" + EOL
				+ "\t\tJean-Philippe" + EOL + "\t</name>" + EOL + "</test>");
		XmlDocument doc = new XmlDocument(elem);
		doc.cleanUp();

		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + EOL
				+ "<test><name>Jean-Philippe</name></test>";

		Assert.assertEquals(expected, doc.toString());
	}

	@Test
	public void testFormat() throws Exception {
		XmlElement root = XmlElement
				.readXML("<employees><employee><firstName>Jean-Philippe</firstName><lastName>Gravel</lastName><dept>Sales</dept><certs><cert>CSDP</cert><cert>RFID Pro</cert></certs></employee></employees>");
		XmlDocument xdoc = new XmlDocument(root);
		xdoc.format(EOL, "  ");
		System.out.println(xdoc);
	}

	@Test
	public void testFormatHugeDoc() throws Exception {
		System.setProperty("entityExpansionLimit", "5000000");
		XmlElement root = XmlElement.readXML(new File("hugedoc.xhtml"));
		XmlDocument xdoc = new XmlDocument(root);
		System.out.println(xdoc);
		xdoc.format(System.getProperty("line.separator"), "  ");
		System.out.println("-------------------------");
		System.out.println(xdoc);
	}

	@Test
	public void testLoadHtml5Document() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("statistics.html");
		Reader reader = new InputStreamReader(is);
		XmlDocument doc = new XmlDocument();
		doc.setDocumentType(XmlDocumentType.XHTML);
		doc.load(reader);
		is.close();

		doc.format();
		StringWriter sw = new StringWriter();
		doc.save(sw);
		sw.close();
		
		System.out.println(sw.toString());
	}
}
