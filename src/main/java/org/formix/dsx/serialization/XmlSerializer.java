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
package org.formix.dsx.serialization;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.binary.Base64;
import org.formix.dsx.XmlContent;
import org.formix.dsx.XmlElement;
import org.formix.dsx.XmlException;
import org.formix.dsx.XmlText;

public class XmlSerializer {

	private HashMap<Class<?>, Class<?>> collectionMap;
	private SortedMap<String, Class<?>> generalTypeMapper;
	private List<String> classResolutionPackages;
	private List<SerializationEventListener> deserializationListeners;

	public XmlSerializer() {
		this.generalTypeMapper = new TreeMap<String, Class<?>>();

		this.collectionMap = new HashMap<Class<?>, Class<?>>();
		this.collectionMap.put(Collection.class, ArrayList.class);
		this.collectionMap.put(List.class, ArrayList.class);
		this.collectionMap.put(Set.class, HashSet.class);
		this.collectionMap.put(SortedSet.class, TreeSet.class);

		this.classResolutionPackages = new ArrayList<String>();

		this.deserializationListeners = new ArrayList<SerializationEventListener>();
	}

	public List<SerializationEventListener> getDeserializationListeners() {
		return deserializationListeners;
	}

	public void addDeserializationListener(SerializationEventListener l) {
		this.deserializationListeners.add(l);
	}

	public void removeDeserializationListener(SerializationEventListener l) {
		this.deserializationListeners.remove(l);
	}

	/**
	 * Returns a map between interfaces and class implementations for any
	 * collection types.
	 * 
	 * Default mappings:
	 * 
	 * Collection -> ArrayList, List -> ArrayList, Set -> HashSet, SortedSet ->
	 * TreeSet
	 * 
	 * @return a map defining which class to instantiate when a Collection is
	 *         encountered in a class member.
	 */
	public HashMap<Class<?>, Class<?>> getCollectionMap() {
		return this.collectionMap;
	}

	public List<String> getClassResolutionPackages() {
		return this.classResolutionPackages;
	}

	/**
	 * Used to quickly map a class simple name to the corresponding Class type.
	 * Mandatory for classes having the XmlRootElement annotation. In the later
	 * case, the XmlRootElement.name (first character capitalized) is used to
	 * map with the class correct type, possibly overriding an existing type
	 * previously mapped during class resolution.
	 * 
	 * @param type
	 *            the type to be added to the general type mapper of the current
	 *            serializer.
	 */
	public void registerClass(Class<?> type) {
		String rootName = type.getSimpleName();
		XmlRootElement xmlRootAnnot = type.getAnnotation(XmlRootElement.class);
		if (xmlRootAnnot != null)
			rootName = this.capitalize(xmlRootAnnot.name());
		this.generalTypeMapper.put(rootName, type);
	}

	public void registerPackage(String packageName) {
		this.classResolutionPackages.add(packageName);
	}

	public Object deserialize(XmlElement root) throws XmlException {
		String simpleName = this.capitalize(root.getName());
		Class<?> type;
		try {
			type = this.getType(simpleName);
		} catch (ClassNotFoundException e) {
			throw new XmlException(
					"Unable to resolve to a type "
							+ "with the given XmlElement root name '"
							+ root.getName()
							+ ". Try to add the right package containing the class "
							+ simpleName
							+ " or use the method XmlSerializer.desirialize(XmlElement, Class<?>) "
							+ "signature instead.", e);
		}
		return this.deserialize(root, type);
	}

	public <T> T deserialize(XmlElement root, Class<T> type)
			throws XmlException {

		if (type.toString().equals("class [B")) {
			@SuppressWarnings("unchecked")
			T value = (T) Base64.decodeBase64(root.getChild(0).toString());
			return value;
		}

		// If the type is a base type from java.util or java.sql or is a
		// collection then decode it directly.
		if (type.getName().startsWith("java.")
				|| Collection.class.isAssignableFrom(type)) {
			@SuppressWarnings("unchecked")
			T value = (T) this.getValue(root, type, null, null);
			return value;
		}

		String rootName = this.capitalize(root.getName());

		String childName = "";
		XmlElement childElem = null;
		if (root.getChild(0) instanceof XmlElement) {
			childElem = (XmlElement) root.getChild(0);
			childName = this.capitalize(childElem.getName());
			if (rootName.equals(childName))
				return this.deserialize(childElem, type);
		}

		T target;
		try {
			target = type.newInstance();
		} catch (Exception e) {
			throw new XmlException("Unable to instanciate type "
					+ type.getName(), e);
		}

		this.onBeforeDeserialization(new SerializationEvent(this, root, target));

		SortedMap<String, Method> methods = this.createMethodMap(type);
		for (XmlContent content : root.getChilds()) {
			if (content instanceof XmlElement) {

				XmlElement elem = (XmlElement) content;
				String methodName = "set" + this.capitalize(elem.getName());
				String signature = methods.tailMap(methodName).firstKey();

				// If the setter is not found for the specified methodName, skip
				// this setter.
				if (signature.startsWith(methodName)) {

					Method setMethod = methods.get(signature);

					Class<?> paramType = setMethod.getParameterTypes()[0];
					try {
						Object value = this.getValue(elem, paramType, methods,
								target);
						setMethod.invoke(target, new Object[] { value });
					} catch (Exception e) {
						String msg = String.format(
								"Unable to assign the XMLElement %s to"
										+ " the property [%s.%s] (%s)", elem,
								type.getName(), setMethod.getName(), signature);
						throw new XmlException(msg, e);
					}

				} else {
					throw new XmlException(String.format(
							"Unable to find the method %s.", methodName));
				}
			}
		}

		this.onAfterDeserialization(new SerializationEvent(this, root, target));

		return target;
	}

	protected void onBeforeDeserialization(SerializationEvent e) {
		for (SerializationEventListener l : this.deserializationListeners) {
			l.beforeDeserialisation(e);
		}
	}

	protected void onAfterDeserialization(SerializationEvent e) {
		for (SerializationEventListener l : this.deserializationListeners) {
			l.afterDeserialisation(e);
		}
	}

	/**
	 * Returns the value of elem as an object of type paramType. The methods and
	 * parent parameters are used in the case that paramType is a subclass of
	 * the Collection type.
	 * 
	 * @see XmlSerializer.decodeCollection for further details.
	 * @param elem
	 *            The xml element to be instantiated to an object of the given
	 *            paramType.
	 * @param paramType
	 *            The type to be created by the getValue.
	 * @param parentMethods
	 *            The method list of the parent.
	 * @param parent
	 *            The instantiated object that will contain the instantiated
	 *            elem.
	 * @return
	 * @throws XmlException
	 */
	private Object getValue(XmlElement elem, Class<?> paramType,
			SortedMap<String, Method> parentMethods, Object parent)
			throws XmlException {
		try {
			SortedMap<String, Method> methods = this.createMethodMap(paramType);

			Method paramTypeValueOfMethod = null;
			if (methods.containsKey("valueOf-String"))
				paramTypeValueOfMethod = methods.get("valueOf-String");

			if (elem.getChilds().size() == 0) {
				if (Collection.class.isAssignableFrom(paramType)) {
					return this.getCollectionValue(elem, paramType,
							parentMethods, parent);
				} else {
					return null;
				}
			}

			Object value = null;
			Iterator<XmlContent> contentIterator = elem.getChilds().iterator();
			XmlContent firstChild = contentIterator.next();
			while (firstChild.toString().trim().equals("")
					&& contentIterator.hasNext()) {
				// skip blank lines
				firstChild = contentIterator.next();
			}

			if (paramType.equals(String.class)) {
				XmlText text = (XmlText) firstChild;
				value = text.getText();

			} else if (paramType.equals(Timestamp.class)) {
				value = new Timestamp(this.parseDate(firstChild.toString())
						.getTime());

			} else if (paramType.equals(Date.class)) {
				value = this.parseDate(firstChild.toString());

			} else if (Calendar.class.isAssignableFrom(paramType)) {
				value = new GregorianCalendar();
				((GregorianCalendar) value).setTime(this.parseDate(firstChild
						.toString()));

			} else if (paramTypeValueOfMethod != null) {
				value = paramTypeValueOfMethod.invoke(null,
						new Object[] { firstChild.toString() });

			} else if (Collection.class.isAssignableFrom(paramType)) {
				value = this.getCollectionValue(elem, paramType, parentMethods,
						parent);

			} else if (Map.class.isAssignableFrom(paramType)) {
				throw new XmlException(
						"The Map deserialization is not yet implemented.");
			} else if (this.isSetter(parentMethods, elem.getName())
					&& (firstChild instanceof XmlElement)) {
				XmlElement elemFirstChild = (XmlElement) firstChild;
				Class<?> specifiedType = this.getType(this
						.capitalize(elemFirstChild.getName()));
				value = this.deserialize(elemFirstChild, specifiedType);
			} else {
				value = this.deserialize(elem, paramType);
			}

			return value;
		} catch (Exception e) {
			throw new XmlException("Problem getting value of " + elem
					+ " of type " + paramType.getName(), e);
		}
	}

	private boolean isSetter(SortedMap<String, Method> parentMethods,
			String elemName) {
		String methodName = "set" + this.capitalize(elemName);
		String signature = parentMethods.tailMap(methodName).firstKey();
		return signature.startsWith(methodName + "-");
	}

	// Date format expected: ISO 8601: 1994-11-05T08:15:30-05:00
	protected Date parseDate(String dateString) throws ParseException {
		Pattern p = Pattern
				.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})([+-]\\d{2}:?\\d{2})?");
		Matcher m = p.matcher(dateString);
		if (m.find()) {
			int[] dateTimeParts = new int[6];
			String timeZone = "";
			for (int g = 1; g <= m.groupCount(); g++) {
				String group = m.group(g);
				if (g < 7)
					dateTimeParts[g - 1] = Integer.parseInt(group);
				else if (m.group(g) != null)
					timeZone = m.group(g);
			}

			Calendar cal = Calendar.getInstance();
			TimeZone tz = TimeZone.getTimeZone("GMT" + timeZone);
			cal.setTimeZone(tz);
			cal.set(Calendar.YEAR, dateTimeParts[0]);
			cal.set(Calendar.MONTH, dateTimeParts[1] - 1);
			cal.set(Calendar.DAY_OF_MONTH, dateTimeParts[2]);
			cal.set(Calendar.HOUR_OF_DAY, dateTimeParts[3]);
			cal.set(Calendar.MINUTE, dateTimeParts[4]);
			cal.set(Calendar.SECOND, dateTimeParts[5]);

			return cal.getTime();
		} else {
			return null;
		}
	}

	protected String formatDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String dateString = String.format("%1$tFT%1$tT", cal);
		String timeZone = String.format("%tz", cal);
		timeZone = timeZone.substring(0, 3) + ":" + timeZone.substring(3);
		dateString += timeZone;
		return dateString;
	}

	/**
	 * This method is called when paramType is an instance of
	 * java.util.Collection. The created collection is then filled with the
	 * child elements.
	 * 
	 * @param elem
	 *            The element representing a collection.
	 * @param paramType
	 *            The type of the desired collection. Before instanciating a
	 *            collection using the internal collectionMap, the algorithm
	 *            tries to obtain the collection from the current parent object
	 *            using the method named "get" + capitalize(elem.getName()). If
	 *            the call succeeds, the existing collection is used. Otherwise,
	 *            a new collection is instantiated using the collectionMap
	 *            definitions.
	 * @param parentMethods
	 *            The method map (<method name>, Method) of the parent object.
	 *            This map is used to obtain the an existing collection object
	 *            in the parent getter corresponding to the current elem name.
	 * @param parent
	 *            The parent object used to get an existing collection, if any.
	 * @return a collection filled with the corresponding child elements.
	 * @throws XmlException
	 */
	@SuppressWarnings("unchecked")
	private Collection<Object> getCollectionValue(XmlElement elem,
			Class<?> paramType, SortedMap<String, Method> parentMethods,
			Object parent) throws XmlException {

		Collection<Object> col = null;
		if ((parentMethods != null) && (parent != null)) {

			String methodName = "get" + this.capitalize(elem.getName());
			String signature = parentMethods.tailMap(methodName).firstKey();
			Method colGetterMethod = parentMethods.get(signature);

			try {
				col = (Collection<Object>) colGetterMethod.invoke(parent,
						(Object[]) null);

			} catch (Exception e) {
				throw new XmlException("Unable to invoke collection getter: "
						+ colGetterMethod.getName(), e);
			}
		}

		try {
			if (col == null)
				col = this.createCollection(paramType);
		} catch (Exception e) {
			throw new XmlException("Unable to create collection type: "
					+ paramType.getName(), e);
		}

		for (XmlContent colContent : elem.getChilds()) {
			if (colContent instanceof XmlElement) {
				XmlElement colElem = (XmlElement) colContent;

				Object objToAdd = null;
				Class<?> colElemType = null;
				String colElemTypeName = this.capitalize(colElem.getName());
				try {
					colElemType = this.getType(colElemTypeName);
				} catch (ClassNotFoundException e) {
					throw new XmlException(
							"Unable to find the collection's internal element type: "
									+ colElemTypeName, e);
				}

				objToAdd = this.deserialize(colElem, colElemType);

				col.add(objToAdd);
			}
		}

		return col;
	}

	public XmlElement serialize(Object target) throws XmlException {
		Class<?> type = target.getClass();
		String rootName = decapitalize(type.getSimpleName());
		return this.serialize(target, rootName);
	}

	public XmlElement serialize(Object target, String rootName)
			throws XmlException {

		Class<?> type = target.getClass();

		XmlRootElement xmlRootAnnot = type.getAnnotation(XmlRootElement.class);
		if (xmlRootAnnot != null)
			rootName = xmlRootAnnot.name();

		XmlElement root = new XmlElement(rootName);

		// If the type is a base type from java.util or java.sql then encode it
		// directly.
		if (type.getName().startsWith("java.")
				|| (target instanceof Collection)) {
			this.addXmlContent(root, target);
			return root;
		}

		if (target instanceof Object[]) {
			this.addXmlContent(root, target);
			return root;
		}

		for (Method method : type.getMethods()) {

			// Ignores XmlTransient methods.
			if (method.getAnnotation(XmlTransient.class) != null)
				continue;

			String methodName = method.getName();
			if ((methodName.startsWith("get") || methodName.startsWith("is"))
					&& !method.getName().equals("getClass")) {

				// If the get method has a parameter, it may be a shortcut
				// accessor to an internal map or collection. We skip this
				// method.
				if (method.getParameterTypes().length > 0)
					continue;

				Object value = null;
				try {
					value = method.invoke(target, (Object[]) null);
				} catch (Exception e) {
					throw new XmlException(e);
				}

				// We don't want null nodes.
				if (value == null)
					continue;

				int idx = 3;
				if (methodName.startsWith("is")) {
					idx = 2;
				}
				String elemName = decapitalize(method.getName().substring(idx));
				XmlElement elem = root.addElement(elemName);
				// Class<?> retType = method.getReturnType();
				this.addXmlContent(elem, value);
			}
		}

		this.registerClass(type);
		return root;
	}

	private void addXmlContent(XmlElement parent, Object value)
			throws XmlException {

		if (value == null) {
			return;
		}

		if ((value instanceof Number) || (value instanceof String)
				|| (value instanceof Boolean)) {
			parent.addText(value.toString());

		} else if (value instanceof Date) {
			parent.addText(this.formatDate((Date) value));

		} else if (value instanceof Calendar) {
			parent.addText(this.formatDate(((Calendar) value).getTime()));

		} else if (value instanceof Enum) {
			parent.addText(value.toString());

		} else if (value instanceof Collection) {
			Collection<?> items = (Collection<?>) value;
			for (Object item : items)
				parent.addChild(this.serialize(item));

		} else if (value instanceof Map) {
			throw new XmlException(
					"The Map serialization is not yet implemented.");
		} else if (value instanceof byte[]) {
			parent.addText(Base64.encodeBase64String((byte[]) value));

		} else if (value instanceof Object[]) {
			for (Object o : (Object[]) value) {
				parent.addChild(this.serialize(o));
			}

		} else {
			parent.addChild(this.serialize(value));
		}
	}

	private String decapitalize(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	private String capitalize(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private SortedMap<String, Method> createMethodMap(Class<?> type) {
		SortedMap<String, Method> map = new TreeMap<String, Method>();
		for (Method m : type.getMethods()) {
			map.put(this.createMethodSignature(m), m);
		}
		return map;
	}

	private String createMethodSignature(Method m) {
		String signature = m.getName();
		for (Class<?> type : m.getParameterTypes())
			signature += "-" + type.getSimpleName();
		return signature;
	}

	@SuppressWarnings("unchecked")
	private Collection<Object> createCollection(Class<?> type)
			throws InstantiationException, IllegalAccessException {

		Class<?> colMappedType = this.collectionMap.get(type);
		if (colMappedType != null)
			return (Collection<Object>) colMappedType.newInstance();

		// If the type isn't mapped, maybe this is the direct collection type
		// implementation. Return the type instantiation.
		return (Collection<Object>) type.newInstance();
	}

	private Class<?> getType(String simpleName) throws ClassNotFoundException {

		if (this.generalTypeMapper.containsKey(simpleName))
			return this.generalTypeMapper.get(simpleName);

		List<String> packages = new ArrayList<String>(
				this.classResolutionPackages);
		packages.add("java.lang");
		packages.add("java.util");
		packages.add("java.sql");
		for (String packageName : packages) {
			String className = packageName + "." + simpleName;
			try {
				Class<?> type = Class.forName(className);
				this.registerClass(type);
				return this.generalTypeMapper.get(simpleName);
			} catch (Exception e) {
				// In this algorithm, catching an exception and doing nothing
				// with it is a normal behavior. This is a violation of the
				// Exception Pattern which says that an exception should be
				// exceptional and handled properly.
				//
				// This "bad" algorithm is a quick and simple (for the
				// programmer(myself)) but rather slow (at runtime) replacement
				// for the correct implementation that should look for the
				// specific classes in the related class path defined by
				// packageSearchOrder and instantiate it if found.
				//
				// Greatly improved efficiency through the usage of the
				// generalTypeMapper but still not a good pattern for class
				// resolution.
			}
		}
		throw new ClassNotFoundException(
				"Unable to instanciate the given type "
						+ simpleName
						+ " with the internal packageSearchOrder "
						+ this.classResolutionPackages
						+ ". Please add the correct package for the type "
						+ simpleName
						+ " using XmlSerializer.getPackageSearchOrder().add(\"{package_name}\")");
	}
}
