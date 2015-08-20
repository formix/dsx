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
package org.formix.dsx.serialization;

import java.util.EventObject;

import org.formix.dsx.XmlElement;

/**
 * Defines a serialization event data.
 * 
 * @author jpgravel
 *
 */
public class SerializationEvent extends EventObject {
	private static final long serialVersionUID = 2332694291964583929L;

	private XmlElement element;
	private Object target;

	/**
	 * Creates a SerializationEvent instance.
	 * 
	 * @param source
	 *            The source of the event.
	 * 
	 * @param element
	 *            The element that caused the event.
	 * 
	 * @param target
	 *            The object to which the given element is related.
	 */
	public SerializationEvent(Object source, XmlElement element, Object target) {
		super(source);
		this.element = element;
		this.target = target;
	}

	/**
	 * Gets the XmlElement of the current event.
	 * 
	 * @return the XmlElement of the current event.
	 */
	public XmlElement getElement() {
		return element;
	}

	/**
	 * Gets the taget of the current event.
	 * 
	 * @return the taget of the current event.
	 */
	public Object getTarget() {
		return target;
	}

}
