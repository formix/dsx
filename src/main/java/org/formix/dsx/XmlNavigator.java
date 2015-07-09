package org.formix.dsx;

import java.util.Collections;
import java.util.LinkedList;

import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;

/**
 * Helps navigate through XML hierarchy using a simple XPath like syntax.
 * 
 * The root element name is always omitted. For example, if you create an
 * XmlNavigator with the following XmlElement as root:
 * 
 * <code>
 *   <root>
 *     <child>Hello</child>
 *   </root>   
 * </code>
 *
 * the following code will return the "Hello" value:
 * 
 * <code>
 * XmlNavigator xnav = new XmlNavigator(root);
 * xnav.getText("/child"); // will return "hello" 
 * </code>
 * 
 * An XmlNavigator never throw exception if a node specified in the path does
 * not exists. If it's the case, it will return the value null which may be
 * confused with the possibility the the node is an empty xml element, i.e.:
 * <code><empty/></code>. To distinguish this case, use the {@code exists}
 * method.
 * 
 * To get a node attribute value, use the following syntax:
 * 
 * <code>
 *   xnav.getText("/child/subItem@attributeName");
 * </code>
 * 
 * The previous code will return the text from the attribute named
 * "attributeName" of the "subItem" node. Once again, to distinguish the
 * standalone attribute and an inexisting attribute, use the {@code exists}
 * method.
 * 
 * @author jpgravel
 *
 */
public class XmlNavigator {

	private XmlElement root;

	public XmlNavigator(XmlElement root) {
		this.root = root;
	}

	public XmlElement getRoot() {
		return root;
	}

	public String getText(String path) {

		String localPath = path;

		String attributeName = this.getAttributeName(path);
		if (attributeName != null) {
			localPath = path.substring(0,
					path.length() - attributeName.length() - 1);
		}

		XmlElement elem = this.getElement(localPath);
		if (elem == null) {
			return null;
		}

		if (attributeName != null) {
			return elem.getAttribute(attributeName);
		}

		StringBuilder sb = new StringBuilder();
		for (XmlContent xmlContent : elem.getChilds()) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			if (!(xmlContent instanceof XmlComment)) {
				sb.append(xmlContent.toString().trim());
			}
		}

		return sb.toString().trim();
	}

	private String getAttributeName(String path) {
		int lastSlash = path.lastIndexOf("/");
		int arobasIndex = path.indexOf('@', lastSlash);
		if (arobasIndex == -1) {
			return null;
		}
		return path.substring(arobasIndex + 1);
	}

	public XmlElement getElement(String path) {
		if (root == null) {
			return null;
		}
		LinkedList<String> names = new LinkedList<String>();
		Collections.addAll(names, path.split("/"));
		XmlElement curr = this.root;
		names.poll(); // removes the root empty name
		String name = names.poll();
		while (name != null) {
			if (curr == null) {
				return null;
			}
			curr = curr.getElement(name);
			name = names.poll();
		}
		return curr;
	}

	public boolean exists(String path) {
		return getElement(path) != null;
	}
}
