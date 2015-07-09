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
 * An interface used to define hooks while xml is read.
 * 
 * @author jpgravel
 *
 */
public interface XmlContentListener {

	/**
	 * Called after an XmlCDATA content is created.
	 * 
	 * @param cdata
	 *            The XmlCDATA content that have just been created.
	 */
	void cdataCreated(XmlCDATA cdata);

	/**
	 * Called after an XmlElement content is created.
	 * 
	 * @param element
	 *            The XmlElement content that have just been created.
	 */
	void elementCreated(XmlElement element);

	/**
	 * Called after an XmlText content is created.
	 * 
	 * @param text
	 *            The XmlText content that have just been created.
	 */
	void textCreated(XmlText text);

	/**
	 * Called after an XmlComment content is created.
	 * 
	 * @param comment
	 *            The XmlComment content that have just been created.
	 */
	void commentCreated(XmlComment comment);

}
