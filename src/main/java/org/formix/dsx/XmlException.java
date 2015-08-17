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
package org.formix.dsx;

/**
 * Exception thrown when a problem occurs during XML parsing.
 * 
 * @author jpgravel
 *
 */
public class XmlException extends Exception {

	private static final long serialVersionUID = -6047208130709180926L;

	/**
	 * Creates an instance of XmlException.
	 */
	public XmlException() {
		super();
	}

	/**
	 * Creates an instance of XmlException with the given message.
	 * 
	 * @param message
	 *            The message associated to the current exception.
	 */
	public XmlException(String message) {
		super(message);
	}

	/**
	 * Creates an instance of XmlException with the given cause.
	 * 
	 * @param cause
	 *            The causs of the current exception.
	 */
	public XmlException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates an instance of XmlException with the given message and cause.
	 * 
	 * @param message
	 *            The message associated to the current exception.
	 *            
	 * @param cause
	 *            The causs of the current exception.
	 */
	public XmlException(String message, Throwable cause) {
		super(message, cause);
	}
}
