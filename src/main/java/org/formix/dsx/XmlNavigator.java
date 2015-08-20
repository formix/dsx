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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;

/**
 * <p>
 * Helps navigate through an XML hierarchy using a simple XPath like syntax.
 * </p>
 * 
 * <p>
 * The root element name is always omitted. For example, if you create an
 * XmlNavigator with the following XmlElement as root:
 * </p>
 * 
 * <p>
 * <code>
 *   &lt;root&gt;
 *     &lt;child&gt;hello&lt;/child&gt;
 *   &lt;/root&gt;   
 * </code>
 * </p>
 *
 * <p>
 * the following code will return "hello":
 * </p>
 * 
 * <p>
 * <code>
 * XmlNavigator xnav = new XmlNavigator(root);
 * xnav.getText("/child"); // will return "hello" 
 * </code>
 * </p>
 * 
 * <p>
 * An XmlNavigator never throw exception if a node specified in the path does
 * not exists. If it's the case, it will return null which may be confused with
 * the possibility the the node is an empty xml element, i.e.:
 * <code>&lt;empty/&gt;</code>. To distinguish this case, use the {@code exists}
 * method.
 * </p>
 * 
 * <p>
 * To get a node attribute value, use the following syntax:
 * </p>
 * 
 * <p>
 * <code>
 *   xnav.getText("/child/subItem@attributeName");
 * </code>
 * </p>
 * 
 * <p>
 * The previous code will return the text from the attribute named
 * "attributeName" of the "subItem" node. Once again, to distinguish the stand
 * alone attribute and an non existent attribute, use the {@code exists} method.
 * </p>
 * 
 * @author jpgravel
 *
 */
public class XmlNavigator {

	private XmlElement root;

	/**
	 * Creates a ne XmlNavigator using the given XmlElement as root.
	 * 
	 * @param root
	 *            The root XmlElement.
	 */
	public XmlNavigator(XmlElement root) {
		this.root = root;
	}

	/**
	 * Gets the root element.
	 * 
	 * @return the root element.
	 */
	public XmlElement getRoot() {
		return root;
	}

	/**
	 * Sets the root element.
	 * 
	 * @param root
	 *            the root element.
	 */
	protected void setRoot(XmlElement root) {
		this.root = root;
	}

	/**
	 * <p>
	 * Gets the concatenated text of all children of the element targeted by the
	 * given path.
	 * </p>
	 * 
	 * <p>
	 * To get an attribute's text, use the following syntax:
	 * </p>
	 * 
	 * <p>
	 * <code>"/container/item@name"</code>
	 * </p>
	 * 
	 * @param path
	 *            the path pointing to an XmlElement or attribute.
	 * 
	 * @return the concatenated text of all children of the element targeted by
	 *         the given path. Returns null if the given path is invalid.
	 */
	public String getText(String path) {

		String localPath = path;

		String attributeName = this.getAttributeName(path);
		if (attributeName != null) {
			localPath = path.substring(0, path.length() - attributeName.length() - 1);
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

	/**
	 * 
	 * @param path
	 * @return
	 */
	public XmlElement getElement(String path) {
		if (root == null) {
			return null;
		}
		LinkedList<String> names = new LinkedList<String>();
		Collections.addAll(names, path.split("/"));
		XmlElement curr = this.root;
		if (path.startsWith("/")) {
			names.poll(); // removes the root empty name
		}
		String name = names.poll();
		while ((name != null) && (curr != null)) {
			Pattern pattern = Pattern.compile("(.*)\\[([0-9]+)\\]");
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
				name = matcher.group(1);
				XmlElement child = curr.getElement(name);
				int index = Integer.parseInt(matcher.group(2));
				List<XmlElement> elements = child.getElements();
				if (index >= elements.size()) {
					curr = null;
				} else {
					curr = elements.get(index);
				}
			} else {
				curr = curr.getElement(name);
			}
			name = names.poll();
		}
		return curr;
	}

	/**
	 * Return all elements that corresponds to the last element name in the
	 * given path.
	 * 
	 * For example:
	 * 
	 * /items/item
	 * 
	 * will return a List containing all &lt;item&gt; elements under
	 * &lt;items&gt;.
	 * 
	 * @param path
	 *            The path to the elements to return.
	 * 
	 * @return A List of elements corresponding to the given path. An empty list
	 *         if the path is invalid or empty.
	 */
	public List<XmlElement> getElements(String path) {
		int lastElemIndex = path.lastIndexOf('/');
		String parentPath = path.substring(0, lastElemIndex);
		XmlElement elem = this.getElement(parentPath);
		if (elem == null) {
			return new ArrayList<XmlElement>(0);
		}
		String elemName = path.substring(lastElemIndex + 1);
		return elem.getElements(elemName);
	}

	/**
	 * Tells if a given path is valid or not.
	 * 
	 * @param path
	 *            The path to test.
	 * 
	 * @return True if the given path is valid, false otherwise.
	 */
	public boolean exists(String path) {
		String localPath = path;
		String attributeName = this.getAttributeName(path);
		if (attributeName != null) {
			localPath = path.substring(0, path.length() - attributeName.length() - 1);
			XmlElement elem = this.getElement(localPath);
			if (elem == null) {
				return false;
			}
			return elem.getAttributes().containsKey(attributeName);
		} else {
			return getElement(path) != null;
		}
	}
}
