package org.formix.dsx.builders;

import org.formix.dsx.XmlDocument;
import org.formix.dsx.XmlElement;
import org.formix.dsx.builders.XmlBuilder;
import org.junit.Test;

public class XmlBuilderTests {

	
	@Test
	public void testNulValues() throws Exception {
		XmlBuilder builder = new XmlBuilder();
		Department d = new Department();
		d.setName("Assembly");
		XmlElement element = builder.buildXml(d);
		XmlDocument doc = new XmlDocument();
		doc.setRoot(element);
		doc.format();
		System.out.println(doc.toString());
	}
	
}
