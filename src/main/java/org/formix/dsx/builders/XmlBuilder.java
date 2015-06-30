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

import org.formix.dsx.XmlComment;
import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;
import org.formix.dsx.XmlException;
import org.formix.dsx.XmlText;

public class XmlBuilder {

	public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XS = "http://www.w3.org/2001/XMLSchema";
	
	private final SimpleDateFormat DATE_FORMAT;

	private Map<String, String> nameSpaces;

	public XmlBuilder() {
		DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
		this.nameSpaces = new LinkedHashMap<String, String>();
	}

	public Map<String, String> getNameSpaces() {
		return nameSpaces;
	}

	public void setNameSpaces(Map<String, String> namespaces) {
		this.nameSpaces = namespaces;
	}

	public XmlElement buildXml(Object obj) throws XmlException {
		XmlElement root = (XmlElement) this.createXml(obj, null);
		for (String nameSpace : this.nameSpaces.keySet()) {
			String nameSpaceDecl = "xmlns";
			String reference = this.nameSpaces.get(nameSpace);
			if (!reference.equals("")) {
				nameSpaceDecl += ":" + reference;
			}
			root.addAttribute(nameSpaceDecl, nameSpace);
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

	private void addPropertyNodes(XmlElement root, Object obj, Class<?> type)
			throws XmlException {
		Method[] methods = this.fetchGetters(type);
		for (Method method : methods) {

			String childName = this.getNodeName(method);
			Object childObj = null;
			try {
				childObj = method.invoke(obj);
			} catch (Exception e) {
				throw new XmlException(String.format(
						"A method invocation error occured on %s.%s",
						type.getName(), method.getName()), e);
			}

			if (childObj != null) {

				Class<?> childType = childObj.getClass();

				XmlContentTypeName useContentName = method
						.getAnnotation(XmlContentTypeName.class);
				if (useContentName != null) {
					childName = this.getNodeName(childType);
				}

				XmlContent child = this.createXml(childObj, childName);

				XmlAttribute attribute = method
						.getAnnotation(XmlAttribute.class);
				if (attribute != null) {
					String value = this.getValue(child);
					root.addAttribute(childName, value);
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
					child.addAttribute(ns + ":nil", "true");
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
				if ((content instanceof XmlText)
						&& !(content instanceof XmlComment)) {
					sb.append(content.toString().trim());
				}
			}
			value = sb.toString();
		} else if ((child instanceof XmlText) && !(child instanceof XmlComment)) {
			value = child.toString().trim();
		}
		return value;
	}

	private void addTypeAttribute(XmlContent child, XmlType xmlType,
			Class<?> childType) throws XmlException {
		String typeName = this.getTypeName(xmlType, childType);
		String prefix = "";
		if (!xmlType.attributeNameSpace().equals("")) {
			String reference = this.getNameSpaceReference(xmlType
					.attributeNameSpace());
			prefix = reference + ":";
		}
		if (child instanceof XmlElement) {
			XmlElement childElement = (XmlElement) child;
			childElement.addAttribute(prefix + "type", typeName);
		}
	}

	private String getTypeName(XmlType xmlType, Class<?> childType)
			throws XmlException {
		if (this.isBasicType(childType)) {
			String prefix = "";
			if (!xmlType.equals("")) {
				String reference = this.getNameSpaceReference(xmlType
						.valueNameSpace());
				prefix = reference + ":";
			}
			return prefix + childType.getSimpleName().toLowerCase();
		} else {
			String prefix = "";
			String reference = this.getNameSpaceReference(childType);
			if (!reference.equals("")) {
				prefix = reference + ":";
			}
			return prefix + childType.getSimpleName();
		}
	}

	private void addArrayItemNodes(XmlElement root, Object obj, Class<?> type)
			throws XmlException {
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
			return new XmlText(DATE_FORMAT.format((Date) obj));
		} else if (Calendar.class.isAssignableFrom(type)) {
			Calendar calendar = (Calendar) obj;
			return new XmlText(DATE_FORMAT.format(calendar.getTime()));
		} else {
			return new XmlText(obj.toString());
		}
	}

	private boolean isBasicType(Class<?> type) {
		return Date.class.isAssignableFrom(type)
				|| Calendar.class.isAssignableFrom(type)
				|| Enum.class.isAssignableFrom(type)
				|| Number.class.isAssignableFrom(type)
				|| String.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type);
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
		String reference = this.getNameSpaceReference(method
				.getDeclaringClass());
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
