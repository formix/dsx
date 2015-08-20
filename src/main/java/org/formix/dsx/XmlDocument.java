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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.formix.dsx.utils.Environment;

/**
 * Represents an XML document with the XML header.
 * 
 * @author jpgravel
 * 
 */
public class XmlDocument extends XmlNavigator implements XmlBloc {

	/**
	 * Creates a map between any XmlContent and it's parent XmlElement. The link
	 * is done between the child XmlContent's id property to it's parent
	 * element.
	 * 
	 * @param root
	 *            the root element on which to start the mapping.
	 * 
	 * @return a map between a child XmlContent's id and a parent XmlElement.
	 */
	public static Map<XmlContent, XmlElement> createParentMap(XmlElement root) {
		Map<XmlContent, XmlElement> map = new HashMap<XmlContent, XmlElement>();
		map.put(root, null);
		createParentMapRecursive(map, root);
		return map;
	}

	private static void createParentMapRecursive(Map<XmlContent, XmlElement> map,
			XmlElement parent) {
		for (XmlContent child : parent.getChilds()) {
			map.put(child, parent);
			if (child instanceof XmlElement) {
				XmlElement elem = (XmlElement) child;
				createParentMapRecursive(map, elem);
			}
		}
	}

	private XmlDocumentType xmlDocumentType;

	/**
	 * Creates an instance of XmlDocument version 1.0
	 */
	public XmlDocument() {
		this(null, XmlDocumentType.XML_1_0);
	}

	/**
	 * Creates an instance of XmlDocument version 1.0, using the given root.
	 * 
	 * @param root
	 *            The root element used for the document.
	 */
	public XmlDocument(XmlElement root) {
		this(root, XmlDocumentType.XML_1_0);
	}

	/**
	 * Creates an instance of XmlDocument with the given doctype.
	 * 
	 * @param doctype
	 *            The document type of the created XmlDocument.
	 */
	public XmlDocument(XmlDocumentType doctype) {
		this(null, doctype);
	}

	/**
	 * Creates an instance of XmlDocument with the given root element and
	 * doctype.
	 * 
	 * @param root
	 *            The root element used for the document.
	 * 
	 * @param doctype
	 *            The document type of the created XmlDocument.
	 */
	public XmlDocument(XmlElement root, XmlDocumentType doctype) {
		super(root);
		this.xmlDocumentType = doctype;
	}

	/**
	 * Sets the root XmlElement.
	 * 
	 * @param root
	 *            the root XmlElement.
	 */
	public void setRoot(XmlElement root) {
		super.setRoot(root);
	}

	/**
	 * Gets the XmlDocumentType.
	 * 
	 * @return the XmlDocumentType.
	 */
	public XmlDocumentType getDocumentType() {
		return xmlDocumentType;
	}

	/**
	 * Sets the XmlDocumentType.
	 * 
	 * @param xmlDocumentType
	 *            The XmlDocumentType.
	 */
	public void setDocumentType(XmlDocumentType xmlDocumentType) {
		this.xmlDocumentType = xmlDocumentType;
	}

	/**
	 * Removes any XmlText in XmlElements that contains white spaces only.
	 */
	public void cleanUp() {
		if (this.getRoot() == null)
			return;
		this.removeWhiteSpaces(this.getRoot());
	}

	private void removeWhiteSpaces(XmlElement node) {
		Iterator<XmlContent> iter = node.getChilds().iterator();
		while (iter.hasNext()) {
			XmlContent child = iter.next();
			if (child instanceof XmlElement) {
				this.removeWhiteSpaces((XmlElement) child);
			} else if (child instanceof XmlText) {
				XmlText text = (XmlText) child;
				if (text.getText().trim().equals("")) {
					iter.remove();
				} else {
					text.setText(text.getText().trim());
				}
			}
		}
	}

	/**
	 * Formats the current document using the default line separator for the
	 * current system and two white spaces as indentation.
	 */
	public void format() {
		this.format(Environment.NEWLINE, "  ");
	}

	/**
	 * Format the XmlDocument using eol to change lines and indent to indent
	 * embedded XmlElements. Before formating, this method will call cleanUp to
	 * trim all XmlText and remove remaining empty XmlText.
	 * 
	 * @param eol
	 *            the end of line characters used to change lines.
	 * 
	 * @param indent
	 *            the indentation string used to indent embedded XmlElements.
	 */
	public void format(String eol, String indent) {
		if (this.getRoot() == null)
			return;
		this.cleanUp();
		this.formatTree(this.getRoot(), eol, 0, indent);
	}

	private void formatTree(XmlElement node, String eol, int level,
			String indent) {

		if (node.getChilds().size() == 0)
			return;

		String currIndent = this.createIndent(level, indent);
		String childIndent = currIndent + indent;

		// If there is only one child node and it's an XmlText node, then the
		// content of the current node is not indented if its length is smaller
		// than 60 characters without any EOL. For all other cases, the content
		// is
		// indented normally.
		boolean doInternalIndentation = true;
		if ((node.getChilds().size() == 1)
				&& (node.getChild(0) instanceof XmlText)) {
			String text = node.getChild(0).toString();
			if ((text.length() < 60) && !text.contains(eol)) {
				doInternalIndentation = false;
			}
		}

		if (doInternalIndentation) {
			List<XmlContent> tmpChilds = new LinkedList<XmlContent>(
					node.getChilds());
			node.getChilds().clear();

			for (XmlContent xmlContent : tmpChilds) {
				if (xmlContent instanceof XmlElement) {
					node.addText(eol + childIndent);
				}
				node.addChild(xmlContent);
				if (xmlContent instanceof XmlElement) {
					XmlElement child = (XmlElement) xmlContent;
					this.formatTree(child, eol, level + 1, indent);
				}
			}

			node.addText(eol + currIndent);
		}

	}

	private String createIndent(int level, String indent) {
		String ret = "";
		for (int i = 0; i < level; i++) {
			ret += indent;
		}
		return ret;
	}

	@Override
	public String toString() {
		try {
			StringWriter sw = new StringWriter();
			this.save(sw);
			return sw.toString();
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof XmlDocument))
			return false;
		XmlDocument other = (XmlDocument) o;
		return this.xmlDocumentType.equals(other.xmlDocumentType)
				&& this.getRoot().deepEquals(other.getRoot());
	}

	/**
	 * Loads the XmlDocument from the specified source file.
	 * 
	 * @param source
	 *            the source file containing the xml data.
	 * 
	 * @throws XmlException
	 *             If a problem with XML is encountered.
	 * 
	 * @throws IOException
	 *             If some file reading problem is encountered.
	 */
	public void load(File source) throws IOException, XmlException {
		Reader reader = new FileReader(source);
		try {
			this.load(reader);
		} finally {
			reader.close();
		}
	}

	/**
	 * Loads the XmlDocument from the specified reader.
	 * 
	 * @param reader
	 *            The reader containing the xml data.
	 * 
	 * @throws XmlException
	 *             If a problem with XML is encountered.
	 * 
	 * @throws IOException
	 *             If some reading problem is encountered.
	 */
	public void load(Reader reader) throws IOException, XmlException {
		this.load(reader, new XmlContentAdapter());
	}

	/**
	 * <p>
	 * Creates a document using the specified reader and XmlContentListener.
	 * Whenever a new XmlContent is created (XmlText, XmlElement, XmlCData or
	 * XmlComment) the corresponding method from the listener is called.
	 * </p>
	 * <p>
	 * This listener allows code to be executed while the document is loading so
	 * that the developer do not need to loop a second time through the document
	 * to get what it need. For example, when loading an html document, somebody
	 * may want to have a rapid access to all elements having the "id" attribute
	 * set using the following code template:
	 * </p>
	 * 
	 * <pre>
	 * {
	 * 	&#064;code
	 * 	XmlDocument htmldoc = new XmlDocument(XmlDocumentType.XHTML);
	 * 	final Map&lt;String, XmlElement&gt; index = new HashMap&lt;String, XmlElement&gt;();
	 * 	XmlContentAdapter adapter = new XmlContentAdapter() {
	 * 		&#064;Override
	 * 		public void elementCreated(XmlElement element) {
	 * 			Map&lt;String, String&gt; attributes = element.getAttributes();
	 * 			if (attributes.containsKey(&quot;id&quot;)) {
	 * 				index.put(attributes.get(&quot;id&quot;), element);
	 * 			}
	 * 		}
	 * 	};
	 * 	htmldoc.load(reader, adapter);
	 * }
	 * </pre>
	 * 
	 * @param reader
	 *            The reader pointing to the XML document to load.
	 * 
	 * @param listener
	 *            A XmlContentListener used to get insights while the document
	 *            is loading.
	 * 
	 * @throws XmlException
	 *             If a problem with XML is encountered.
	 * 
	 * @throws IOException
	 *             If some reading problem is encountered.
	 */
	public void load(Reader reader, XmlContentListener listener)
			throws IOException, XmlException {
		this.setRoot(XmlElement.readXML(reader, listener));
	}

	/**
	 * Saves the current document in the specified target file.
	 * 
	 * @param target
	 *            The file where the xml document will be saved.
	 * 
	 * @throws IOException
	 *             If some problems occurs while saving XML to the file.
	 */
	public void save(File target) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(target), "UTF-8");
		this.save(writer);
		writer.close();
	}

	/**
	 * Save the current XmlDocument to the specified writer.
	 * 
	 * @param writer
	 *            The writer used to save the xml document.
	 * 
	 * @throws IOException
	 *             If an issue happens while writing to the writer.
	 */
	public void save(Writer writer) throws IOException {
		PrintWriter pw = new PrintWriter(writer);
		if (this.xmlDocumentType == XmlDocumentType.XHTML) {
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		} else if (this.xmlDocumentType == XmlDocumentType.XML_1_0) {
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		} else if (this.xmlDocumentType == XmlDocumentType.XML_1_1) {
			pw.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>");
		}
		if (this.getRoot() != null) {
			this.getRoot().write(writer);
			pw.flush();
		}
	}

	/**
	 * Saves the current document to the given output stream. The XML will be
	 * written in UTF8.
	 * 
	 * @param out
	 *            The output stream to write to.
	 * 
	 * @throws IOException
	 *             If an error occurs while saving.
	 */
	public void save(OutputStream out) throws IOException {
		this.save(new OutputStreamWriter(out, Charset.availableCharsets().get(
				"UTF-8")));
	}

	/**
	 * Creates a map between any XmlContent and it's parent XmlElement. The link
	 * is done between the child XmlContent's id property to it's parent
	 * element.
	 * 
	 * @return a map between a child XmlContent's id and a parent XmlElement.
	 */
	public Map<XmlContent, XmlElement> createParentMap() {
		return createParentMap(this.getRoot());
	}
}
