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
 * An Adapter with an empty implementation for each of XmlContentListener
 * methods.
 * 
 * @author jpgravel
 *
 */
public class XmlContentAdapter implements XmlContentListener {

	@Override
	public void cdataCreated(XmlCDATA cdata) {
	}

	@Override
	public void elementCreated(XmlElement element) {
	}

	@Override
	public void textCreated(XmlText text) {
	}

	@Override
	public void commentCreated(XmlComment comment) {
	}

}
