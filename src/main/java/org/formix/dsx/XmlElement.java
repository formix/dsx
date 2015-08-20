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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.formix.dsx.utils.SUID;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Define an Xml node in an XML document.
 * 
 * @author jpgravel
 *
 */
public class XmlElement implements XmlContent {

	/**
	 * Creates a new XmlElement.
	 * 
	 * @param name
	 *            The name of the node to create.
	 * 
	 * @return A new XmlElement.
	 */
	public static XmlElement create(String name) {
		return new XmlElement(name);
	}

	private static SAXParser createSaxParser()
			throws ParserConfigurationException, SAXNotRecognizedException,
			SAXNotSupportedException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);

		factory.setValidating(false);
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature(
				"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
				false);
		factory.setFeature(
				"http://apache.org/xml/features/nonvalidating/load-external-dtd",
				false);

		SAXParser parser = factory.newSAXParser();
		return parser;
	}

	/**
	 * Reads an XmlElement from the given file. Every sub child xml content will
	 * be recurseively created.
	 * 
	 * @param file
	 *            The file containing the XmlElement.
	 * 
	 * @return A new XmlElement.
	 * 
	 * @throws XmlException
	 *             If there is any problem during Xml parsing.
	 */
	public static XmlElement readXML(File file) throws XmlException {
		try {
			Reader reader = new FileReader(file);
			XmlElement element = readXML(reader);
			reader.close();
			return element;
		} catch (FileNotFoundException e) {
			throw new XmlException("Invalid file " + file, e);
		} catch (IOException e) {
			throw new XmlException("Unable to read the file " + file, e);
		}
	}

	/**
	 * Reads an XmlElement from the given reader. Every sub child xml content
	 * will be recurseively created.
	 * 
	 * @param reader
	 *            The reader containing the XmlElement.
	 * 
	 * @return A new XmlElement.
	 * 
	 * @throws XmlException
	 *             If there is any problem during Xml parsing.
	 */
	public static XmlElement readXML(Reader reader) throws XmlException {
		return readXML(reader, new XmlContentAdapter());
	}

	/**
	 * Read XML from the given reader and fire events from the provided
	 * listener.
	 * 
	 * @param reader
	 *            The reader containing the XML data.
	 * 
	 * @param listener
	 *            The XmlContentListener containing code to be notified each
	 *            time an Xml content object is created.
	 * 
	 * @return The XmsElement created by parsing the given reader data.
	 * 
	 * @throws XmlException
	 *             Thrown if a problem occur while reading the provided XML
	 *             data.
	 */
	public static XmlElement readXML(Reader reader, XmlContentListener listener)
			throws XmlException {
		XmlHandler handler = new XmlHandler(listener);
		try {
			SAXParser parser = createSaxParser();
			parser.setProperty("http://xml.org/sax/properties/lexical-handler",
					handler);
			parser.parse(new InputSource(reader), handler);
			return handler.getRootElement();
		} catch (SAXException e) {
			String message = "A parser problem occured";
			if (handler.peekTopElement() != null) {
				message += ", node = " + handler.peekTopElement().toString();
			}
			throw new XmlException(message, e);
		} catch (IOException e) {
			throw new XmlException("A reader problem occured.", e);
		} catch (ParserConfigurationException e) {
			throw new XmlException("A parser configuration has been detected.",
					e);
		}
	}

	/**
	 * Parse the given xml string into an XmlElement.
	 * 
	 * @param xml
	 *            The XML string to parse.
	 * 
	 * @return The XmlElement parsed from the given XML string.
	 * 
	 * @throws XmlException
	 *             Thrown if a problem occur while reading the provided XML
	 *             data.
	 */
	public static XmlElement readXML(String xml) throws XmlException {
		try {
			Reader reader = new StringReader(xml);
			XmlElement element = readXML(reader);
			reader.close();
			return element;
		} catch (IOException e) {
			throw new XmlException(
					"A problem occured when closing the reader.", e);
		}
	}

	private Map<String, String> attributes;
	private List<XmlContent> childs;
	private long id;
	private String name;

	/**
	 * Creates an XmlElement instance with the given name.
	 * 
	 * @param name
	 *            The name given to the XmlElement.
	 */
	public XmlElement(String name) {
		this.setName(name);
		this.attributes = new LinkedHashMap<String, String>();
		this.childs = new ArrayList<XmlContent>();
		this.id = -1;
	}

	/**
	 * Adds an attribute to the current XmlElement.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * 
	 * @param value
	 *            The value of the attribute.
	 * 
	 * @return The current XmlElement instance for method chaining.
	 * 
	 * @deprecated This method is deprecated. Use {@code setAttribute} method
	 *             instead.
	 */
	@Deprecated
	public XmlElement addAttribute(String name, String value) {
		this.attributes.put(name, value);
		return this;
	}

	/**
	 * Adds a new XmlCDATA text to the current XmlElement.
	 * 
	 * @param text
	 *            The CDATA text.
	 * 
	 * @return The XmlCDATA instance added to the current XmlElement.
	 */
	public XmlCDATA addCDATA(String text) {
		XmlCDATA cdata = new XmlCDATA(text);
		this.addChild(cdata);
		return cdata;
	}

	/**
	 * Adds the given XmlContent to the current to the current XmlElement
	 * instance.
	 * 
	 * @param child
	 *            The child XmlContent to add.
	 * 
	 * @return The current XmlElement instance for method chaining.
	 */
	public XmlElement addChild(XmlContent child) {
		this.childs.add(child);
		return this;
	}

	/**
	 * Adds a new XmlComment to the current XmlElement.
	 * 
	 * @param text
	 *            The text of the XmlComment.
	 * 
	 * @return The newly created XmlComment.
	 */
	public XmlComment addComment(String text) {
		XmlComment comment = new XmlComment(text);
		this.addChild(comment);
		return comment;
	}

	/**
	 * Adds a new XmlElement to the current XmlElement.
	 * 
	 * @param name
	 *            The name of the XmlElement.
	 * 
	 * @return The newly created XmlElement.
	 */
	public XmlElement addElement(String name) {
		XmlElement element = new XmlElement(name);
		this.addChild(element);
		return element;
	}

	/**
	 * Adds a new XmlText content to the current XmlElement.
	 * 
	 * @param text
	 *            The text of the XmlText.
	 * 
	 * @return The newly created XmlText.
	 */
	public XmlText addText(String text) {
		XmlText txt = new XmlText(text);
		this.addChild(txt);
		return txt;
	}

	/**
	 * Compare the actual XmlElement along whit it's childs.
	 * 
	 * @param o
	 *            The other element to do the comparison with.
	 * 
	 * @return True if bots XmlElements are equals, contains the same number of
	 *         childs and all those childs are equal to each other, recursively.
	 */
	public boolean deepEquals(XmlElement o) {
		if (!this.equals(o))
			return false;

		if (this.childs.size() != o.childs.size())
			return false;

		for (XmlContent c1 : this.childs) {
			boolean atLeastOneEqual = false;

			for (int i = 0; i < o.childs.size() && !atLeastOneEqual; i++) {
				XmlContent c2 = o.childs.get(i);
				if (c1 instanceof XmlElement && c2 instanceof XmlElement) {
					XmlElement e1 = (XmlElement) c1;
					XmlElement e2 = (XmlElement) c2;
					atLeastOneEqual = e1.deepEquals(e2);
				} else if (c1.equals(c2)) {
					atLeastOneEqual = true;
				}
			}

			if (!atLeastOneEqual)
				return false;
		}

		return true;
	}
	
	

	/**
	 * Two XmlElements are equals if they have the same name, the same number of
	 * attributes and all those attributes contains the same values.
	 * Sub-elements are not compared.
	 * 
	 * @param o
	 *            The object to do the comparison with.
	 * 
	 * @return true if both XmlElement are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof XmlElement))
			return false;

		XmlElement other = (XmlElement) o;
		
		if (this.id != other.id) {
			return false;
		}

		if (!this.name.equals(other.name))
			return false;

		if (this.attributes.size() != other.attributes.size())
			return false;

		for (String key : this.attributes.keySet()) {
			if (!other.attributes.containsKey(key))
				return false;
			if (!this.attributes.get(key).equals(other.attributes.get(key)))
				return false;
		}

		return true;
	}
	
	@Override
	public int hashCode() {
		return XmlElement.class.getName().hashCode() ^ 
				Long.hashCode(this.id);
	}

	/**
	 * Tells if the given attribute name exists in the current XmlElement.
	 * 
	 * @param name
	 *            The name of the seeked attribute.
	 * 
	 * @return True if the current XmlElement has the named attribute, false
	 *         otherwise.
	 */
	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}

	/**
	 * Gets the named attribute value.
	 * 
	 * @param name
	 *            The name of the desired attribute.
	 * 
	 * @return The value of the named attribute or null if the attribute does
	 *         not exists for the current XmlElement.
	 */
	public String getAttribute(String name) {
		return this.attributes.get(name);
	}

	/**
	 * Gets the attribute map of the current XmlElement.
	 * 
	 * @return A Map&lt;String, String&gt; containing all attributes of this
	 *         XmlElement.
	 */
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	/**
	 * Gets the child XmlContent at the given index of the current XmlElement.
	 * 
	 * @param index
	 *            The index of the child to get.
	 * 
	 * @return The child XmlContent at index.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range.
	 */
	public XmlContent getChild(int index) {
		return this.childs.get(index);
	}

	/**
	 * Gets this XmlContent instance child list.
	 * 
	 * @return This XmlContent child list.
	 */
	public List<XmlContent> getChilds() {
		return this.childs;
	}

	/**
	 * Concatenate all child's XmlContent.toString() trimmed and separated by a
	 * single space of the named XmlElement.
	 * <p>
	 * for example:<br>
	 * given the following XML hierarchy: - root - child - "string value"
	 * </p>
	 * <p>
	 * Executing:<br>
	 * root.getValue("child")<br>
	 * </p>
	 * <p>
	 * Will return:<br>
	 * "string value"
	 * </p>
	 * 
	 * @param elementName
	 *            The name of the child element for which we want the inner
	 *            value.
	 * 
	 * @return The content of the named child element.
	 */
	public String getValue(String elementName) {
		StringBuilder sb = new StringBuilder();
		List<XmlContent> grandChilds = this.getElement(elementName).getChilds();
		for (XmlContent xmlContent : grandChilds) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(xmlContent.toString().trim());
		}
		return sb.toString();
	}

	/**
	 * Return the first occurence of the named child XmlElement.
	 * 
	 * @param name
	 *            The name of the desired child element.
	 * 
	 * @return The named element or null if there is no such child XmlElement.
	 */
	public XmlElement getElement(String name) {
		return this.getElement(name, 0);
	}

	/**
	 * Gets the named child element at the given index.
	 * 
	 * @param name
	 *            The name of the child XmlElement desired.
	 * 
	 * @param index
	 *            The index of the desired XmlElement.
	 * 
	 * @return The child XmlElement corresponding to the given name at the given
	 *         index or null if no element is fount at this index.
	 * 
	 */
	public XmlElement getElement(String name, int index) {
		int counter = 0;
		for (XmlContent child : this.childs) {
			if (child instanceof XmlElement) {
				XmlElement element = (XmlElement) child;
				if (element.getName().equals(name)) {
					if (counter == index)
						return element;
					counter++;
				}
			}
		}
		return null;
	}

	/**
	 * Returns all child XmlElement having the given name.
	 * 
	 * @param name
	 *            The name of the desired child XmlElements
	 * 
	 * @return A List of child XmlElement.
	 */
	public List<XmlElement> getElements(String name) {
		ArrayList<XmlElement> elements = new ArrayList<XmlElement>();
		for (XmlContent child : this.childs) {
			if (child instanceof XmlElement) {
				XmlElement element = (XmlElement) child;
				if (element.getName().equals(name))
					elements.add(element);
			}
		}
		return elements;
	}

	
	/**
	 * Gets a list of all XmlElement children.
	 * 
	 * @return a list of all XmlElement children.
	 */
	public List<XmlElement> getElements() {
		ArrayList<XmlElement> elements = new ArrayList<XmlElement>();
		for (XmlContent content : this.childs) {
			if (content instanceof XmlElement) {
				elements.add((XmlElement) content);
			}
		}
		return elements;
	}

	

	@Override
	public long getId() {
		if (this.id == -1) {
			this.id = SUID.nextId();
		}
		return this.id;
	}

	/**
	 * Gets the name of the current XmlElement.
	 * 
	 * @return the name of the current XmlElement.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a value to the given attribute.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * 
	 * @param value
	 *            The value of the attribute.
	 * 
	 * @return The current XmlElement instance for method chaining.
	 * 
	 */
	public XmlElement setAttribute(String name, String value) {
		this.attributes.put(name, value);
		return this;
	}

	/**
	 * Sets the name of the current element.
	 * 
	 * @param name
	 *            The new name of the current attribute.
	 */
	public void setName(String name) {
		if (name.matches(".*\\s.*"))
			throw new IllegalArgumentException(
					"The element's name can't contain white spaces.");
		this.name = name;
	}

	@Override
	public String toString() {
		return this.toXml();
	}

	@Override
	public String toXml() {
		StringWriter sw = new StringWriter();
		try {
			this.write(sw);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sw.toString();
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("<");
		writer.append(this.name);

		for (String name : this.attributes.keySet()) {
			writer.write(' ');
			this.writeAttribute(writer, name);
		}

		if (this.childs.size() == 0)
			writer.append("/");

		writer.append(">");

		for (XmlContent content : this.childs)
			content.write(writer);

		if (this.childs.size() > 0) {
			writer.append("</");
			writer.append(this.name);
			writer.append(">");
		}
	}

	private void writeAttribute(Writer writer, String name) throws IOException {
		writer.append(name);
		String value = this.attributes.get(name);
		if (value != null) {
			writer.append("=\"");
			writer.append(StringEscapeUtils.escapeXml(value));
			writer.append("\"");
		}
	}

}
