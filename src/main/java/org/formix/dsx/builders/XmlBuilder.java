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
package org.formix.dsx.builders;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.formix.dsx.XmlComment;
import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;
import org.formix.dsx.XmlException;
import org.formix.dsx.XmlText;

/**
 * Generates XML using xml attributes to modulate how the xml serialization will
 * behave. This class will hoist all namespace definitions to the root element
 * of the final XML output. The first namespace encoutered will be set as the
 * default name space. Further namespaces will have reference names like "ns1",
 * "ns2", etc.
 * 
 * @author jpgravel
 *
 */
public class XmlBuilder {

	public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XS = "http://www.w3.org/2001/XMLSchema";

	private final SimpleDateFormat DATE_FORMAT;

	private Map<String, String> nameSpaces;

	/**
	 * Creates an instance of the XmlBuilder.
	 */
	public XmlBuilder() {
		DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
		this.nameSpaces = new LinkedHashMap<String, String>();
	}

	/**
	 * <p>
	 * Gets namespace reference entries. The map is done between the actual
	 * namespace uri and the given reference name for this namespace. A
	 * namespace reference map entry could be:
	 * </p>
	 * 
	 * <p>
	 * {"http://schemas.xmlsoap.org/soap/envelope/", "ns1"}
	 * </p>
	 * 
	 * @return namespace reference entries.
	 */
	public Map<String, String> getNameSpaces() {
		return nameSpaces;
	}

	/**
	 * Sets namespace reference entries. You can define precisely the namespace
	 * reference name you want to have for each namespace uri.
	 * 
	 * @param namespaces
	 *            the namespace map to use.
	 */
	public void setNameSpaces(Map<String, String> namespaces) {
		if (namespaces == null) {
			throw new NullArgumentException("namespaces");
		}
		this.nameSpaces = namespaces;
	}

	/**
	 * Builds the XML tree with the given object.
	 * 
	 * @param obj
	 *            The object to serialize to XML.
	 * 
	 * @return the root XmlElement of the resulting serialization.
	 * 
	 * @throws XmlException
	 *             If a problem occurs during serialization.
	 */
	public XmlElement buildXml(Object obj) throws XmlException {
		XmlElement root = (XmlElement) this.createXml(obj, null);
		for (String nameSpace : this.nameSpaces.keySet()) {
			String nameSpaceDecl = "xmlns";
			String reference = this.nameSpaces.get(nameSpace);
			if (!reference.equals("")) {
				nameSpaceDecl += ":" + reference;
			}
			root.setAttribute(nameSpaceDecl, nameSpace);
		}
		return root;
	}

	private XmlContent createXml(Object obj, String name) throws XmlException {

		Class<?> type = obj.getClass();
		String rootName = name;
		if (rootName == null) {
			rootName = this.getNodeName(type);
		}

		XmlElement root = new XmlElement(rootName);
		if (this.isBasicType(type)) {
			this.addTextContent(root, obj, type);

		} else if (this.isEnumerable(type)) {
			this.addArrayItemNodes(root, obj, type);

		} else {
			this.addPropertyNodes(root, obj, type);
		}

		return root;
	}

	private void addTextContent(XmlElement root, Object obj, Class<?> type) {
		XmlText text = this.createText(obj, type);
		if (text != null) {
			root.addChild(text);
		}
	}

	private void addPropertyNodes(XmlElement root, Object obj, Class<?> type) throws XmlException {
		Method[] methods = this.fetchGetters(type);
		for (Method method : methods) {

			String childName = this.getNodeName(method);
			Object childObj = null;
			try {
				childObj = method.invoke(obj);
			} catch (Exception e) {
				throw new XmlException(
						String.format("A method invocation error occured on %s.%s", type.getName(), method.getName()),
						e);
			}

			if (childObj != null) {

				Class<?> childType = childObj.getClass();

				XmlContentTypeName useContentName = method.getAnnotation(XmlContentTypeName.class);
				if (useContentName != null) {
					childName = this.getNodeName(childType);
				}

				XmlContent child = this.createXml(childObj, childName);

				XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);
				if (attribute != null) {
					String value = this.getValue(child);
					root.setAttribute(childName, value);
				} else {
					XmlType xmlType = method.getAnnotation(XmlType.class);
					if (xmlType != null) {
						this.addTypeAttribute(child, xmlType, childType);
					}
					root.addChild(child);
				}
			} else {
				XmlExplicitNull explicitNull = method.getAnnotation(XmlExplicitNull.class);
				if (explicitNull != null) {
					XmlElement child = new XmlElement(childName);
					String ns = this.getNameSpaceReference(XSI);
					child.setAttribute(ns + ":nil", "true");
					root.addChild(child);
				}
			}
		}
	}

	private String getValue(XmlContent child) {
		String value = "";
		if (child instanceof XmlElement) {
			XmlElement childElement = (XmlElement) child;
			StringBuilder sb = new StringBuilder();
			for (XmlContent content : childElement.getChilds()) {
				if (sb.length() > 0) {
					sb.append(' ');
				}
				if ((content instanceof XmlText) && !(content instanceof XmlComment)) {
					sb.append(content.toString().trim());
				}
			}
			value = sb.toString();
		} else if ((child instanceof XmlText) && !(child instanceof XmlComment)) {
			value = child.toString().trim();
		}
		return value;
	}

	private void addTypeAttribute(XmlContent child, XmlType xmlType, Class<?> childType) throws XmlException {
		String typeName = this.getTypeName(xmlType, childType);
		String prefix = "";
		if (!xmlType.attributeNameSpace().equals("")) {
			String reference = this.getNameSpaceReference(xmlType.attributeNameSpace());
			prefix = reference + ":";
		}
		if (child instanceof XmlElement) {
			XmlElement childElement = (XmlElement) child;
			childElement.setAttribute(prefix + "type", typeName);
		}
	}

	private String getTypeName(XmlType xmlType, Class<?> childType) throws XmlException {
		if (this.isBasicType(childType)) {
			String prefix = "";
			if (!xmlType.equals("")) {
				String reference = this.getNameSpaceReference(xmlType.valueNameSpace());
				prefix = reference + ":";
			}
			String typeName = childType.getSimpleName().toLowerCase();
			if (typeName.equals("integer")) {
				typeName = "int";
			} else if (typeName.equals("bigdecimal")) {
				typeName = "decimal";
			} else if (typeName.equals("date") || typeName.equals("calendar")) {
				typeName = "dateTime";
			}
			return prefix + typeName;
		} else {
			String prefix = "";
			String reference = this.getNameSpaceReference(childType);
			if (!reference.equals("")) {
				prefix = reference + ":";
			}
			return prefix + childType.getSimpleName();
		}
	}

	private void addArrayItemNodes(XmlElement root, Object obj, Class<?> type) throws XmlException {
		if (type.isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				Object item = Array.get(obj, i);
				XmlContent content = this.createXml(item, null);
				root.addChild(content);
			}
		} else {
			Collection<?> elements = (Collection<?>) obj;
			for (Object element : elements) {
				XmlContent content = this.createXml(element, null);
				root.addChild(content);
			}
		}
	}

	private boolean isEnumerable(Class<?> type) {
		return type.isArray() || Collection.class.isAssignableFrom(type);
	}

	private XmlText createText(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (Date.class.isAssignableFrom(type)) {
			return new XmlText(formatDate((Date) obj));
		} else if (Calendar.class.isAssignableFrom(type)) {
			Calendar calendar = (Calendar) obj;
			return new XmlText(formatDate(calendar.getTime()));
		} else {
			return new XmlText(obj.toString());
		}
	}

	/*
	 * In Java 6, the time zone cannot be formatted the way we want it for Xml
	 * We must add the ':' in the time zone numbers. In Java 7, this can be done
	 * using the XXX pattern with the SimpleDateFormat but this ain't supported
	 * in previous versions
	 * 
	 * @param date The date to be formatted
	 * 
	 * @return The formatted date according to
	 * http://books.xmlschemata.org/relaxng/ch19-77049.html
	 */
	private String formatDate(Date date) {
		String formattedDate = "";
		formattedDate = DATE_FORMAT.format(date);
		if (StringUtils.isNotEmpty(formattedDate)) {
			int l = formattedDate.length();
			l = l - 2;
			String left = formattedDate.substring(0, l);
			String right = formattedDate.substring(l);
			formattedDate = left + ":" + right;
		}
		return formattedDate;
	}

	private boolean isBasicType(Class<?> type) {
		return Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)
				|| Enum.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type)
				|| String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type);
	}

	private Method[] fetchGetters(Class<?> type) {
		Method[] methods = type.getMethods();
		List<Method> ret = new ArrayList<Method>();
		for (Method method : methods) {
			String methodName = method.getName();
			boolean isGetter = Pattern.matches("^(get|is).+", methodName);
			if (isGetter && !methodName.equals("getClass")) {
				XmlIgnore ignore = method.getAnnotation(XmlIgnore.class);
				if (ignore == null) {
					ret.add(method);
				}
			}
		}

		Collections.sort(ret, new Comparator<Method>() {
			public int compare(Method m1, Method m2) {

				int order1 = Integer.MIN_VALUE;
				XmlOrder xorder1 = m1.getAnnotation(XmlOrder.class);
				if (xorder1 != null) {
					order1 = xorder1.value();
				}

				int order2 = Integer.MIN_VALUE;
				XmlOrder xorder2 = m2.getAnnotation(XmlOrder.class);
				if (xorder2 != null) {
					order2 = xorder2.value();
				}

				int diff = order1 - order2;
				if (diff == 0) {
					diff = m1.getName().compareTo(m2.getName());
				}

				return diff;
			}
		});
		return ret.toArray(new Method[ret.size()]);
	}

	private String getNodeName(Method method) throws XmlException {

		String name = method.getName();
		if (name.startsWith("is")) {
			name = name.substring(2, name.length());
		} else if (name.startsWith("get")) {
			name = name.substring(3, name.length());
		}

		XmlName nameAttr = method.getAnnotation(XmlName.class);
		if (nameAttr != null) {
			name = nameAttr.value();
		}

		String prefix = "";
		String reference = getNameSpaceReference(method);
		if (!reference.equals("")) {
			prefix = reference + ":";
		}

		name = prefix + name;

		return name;
	}

	private String getNodeName(Class<?> type) throws XmlException {
		String name = type.getSimpleName();
		XmlName nameAttr = type.getAnnotation(XmlName.class);
		if (nameAttr != null) {
			name = nameAttr.value();
		}

		String prefix = "";
		String reference = this.getNameSpaceReference(type);
		if (!reference.equals("")) {
			prefix = reference + ":";
		}

		name = prefix + name;

		return name;
	}

	private String getNameSpaceReference(Method method) throws XmlException {
		String reference = this.getNameSpaceReference(method.getDeclaringClass());
		XmlNameSpace nsAttr = method.getAnnotation(XmlNameSpace.class);
		if (nsAttr != null) {
			reference = this.getNameSpaceReference(nsAttr);
		}
		return reference;
	}

	private String getNameSpaceReference(Class<?> type) throws XmlException {
		XmlNameSpace nsAttr = null;
		nsAttr = type.getAnnotation(XmlNameSpace.class);
		return this.getNameSpaceReference(nsAttr);
	}

	private String getNameSpaceReference(XmlNameSpace nsAttr) {
		String nameSpace = "http://tempuri.org/";
		if (nsAttr != null) {
			nameSpace = nsAttr.value();
		}
		return getNameSpaceReference(nameSpace);
	}

	private String getNameSpaceReference(String nameSpace) {
		String reference = "";
		if (this.nameSpaces.containsKey(nameSpace)) {
			reference = this.nameSpaces.get(nameSpace);
		} else {
			if (this.nameSpaces.size() > 0) {
				reference = "ns" + this.nameSpaces.size();
			}
			this.nameSpaces.put(nameSpace, reference);
		}
		return reference;
	}

}
