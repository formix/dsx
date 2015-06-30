package org.formix.dsx;

import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;

public class XmlNavigator {

	private XmlElement root;
	
	public XmlNavigator(XmlElement root) {
		this.root = root;
	}

	public XmlElement getRoot() {
		return root;
	}
	
	public String getText(String path) {

		if (path.lastIndexOf('@') != -1) {
			return this.getAttribute(path);
		}
		
		XmlElement curr = this.getElement(path);
		if (curr == null){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (XmlContent xmlContent : curr.getChilds()) {
			sb.append(xmlContent.toString());
		}
		return sb.toString().trim();
	}
	
	private String getAttribute(String path) {
		
		int lastSeparatorIndex = path.lastIndexOf('/');

		String newPath = path.substring(0, lastSeparatorIndex);
		XmlElement curr = this.getElement(newPath);
		if (curr == null){
			return null;
		}
		
		String attributeName = path.substring(lastSeparatorIndex + 2);
		if (curr.getAttributes().containsKey(attributeName)) {
			String attributeValue = curr.getAttribute(attributeName);
			if (attributeValue == null) {
				attributeValue = "";
			}
			return attributeValue;
		}
		
		return null;
	}

	public XmlElement getElement(String path) {
		XmlElement curr = null;

		if (root != null) {
			String[] elemNames = path.split("/");
			for (String elemName : elemNames) {
				if (elemName.equals("")) {
					curr = root;
				} else {
					XmlElement child = curr.getElement(elemName);
					if (child == null) {
						return null;
					}
					curr = child;
				}
			}
		}
		
		return curr;
	}
	
	public boolean exists(String path) {
		return getElement(path) != null;
	}
}
