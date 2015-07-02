package org.formix.dsx;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

public class XmlNavigatorTests {

	public XmlNavigatorTests() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testGetValue() throws Exception {
		XmlDocument indoc = new XmlDocument();

		InputStream is = this.getClass().getResourceAsStream("idoc_test1.xml");
		InputStreamReader isr = new InputStreamReader(is);
		
		indoc.load(isr);
		
		XmlNavigator xnav = new XmlNavigator(indoc.getRoot());
		
		String nothing = xnav.getText("/IDOC/BOB/CASHFLOW");
		Assert.assertNull(nothing);
				
		String name1 = xnav.getText("/IDOC/E1KNA1M/NAME1");
		Assert.assertEquals("Patrick's company1234", name1);
		
		
	}

	@Test
	public void testGetElement() throws Exception {

		XmlDocument indoc = new XmlDocument();

		InputStream is = this.getClass().getResourceAsStream("idoc_test1.xml");
		InputStreamReader isr = new InputStreamReader(is);
		
		indoc.load(isr);
		
		XmlNavigator xnav = new XmlNavigator(indoc.getRoot());
		
		XmlElement nothing = xnav.getElement("/IDOC/BOB/CASHFLOW");
		Assert.assertNull(nothing);
				
		XmlElement name1 = xnav.getElement("/IDOC/E1KNA1M/NAME1");
		Assert.assertEquals("NAME1", name1.getName());

	}

	@Test
	public void testExists() throws Exception {
		XmlDocument indoc = new XmlDocument();

		InputStream is = this.getClass().getResourceAsStream("idoc_test1.xml");
		InputStreamReader isr = new InputStreamReader(is);
		
		indoc.load(isr);
		
		XmlNavigator xnav = new XmlNavigator(indoc.getRoot());
		
		Assert.assertFalse(xnav.exists("/IDOC/BOB/CASHFLOW"));
		Assert.assertFalse(xnav.exists("/IDOC/E1KNA1M/NONE"));
		Assert.assertTrue(xnav.exists("/IDOC/E1KNA1M/ANRED"));
		Assert.assertTrue(xnav.exists("/IDOC/E1KNA1M/E1KNVVM/SPART"));
		
		XmlNavigator xnav2 = new XmlNavigator(xnav.getElement("/IDOC/E1KNA1M/E1KNVVM"));
		Assert.assertTrue(xnav2.exists("/SPART"));
		Assert.assertFalse(xnav2.exists("/BOB"));
		
	}
	
	
	@Test
	public void testGetAttributeText() throws Exception {
		String xml = "<root><item><child name=\"test\"/></item></root>";
		XmlElement root = XmlElement.readXML(xml);
		XmlNavigator xnav = new XmlNavigator(root);
		String name = xnav.getText("/item/child@name");
		Assert.assertEquals("test", name);
	}

}
