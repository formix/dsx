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

import java.io.IOException;
import java.io.Writer;

/**
 * Defines the methods found in all XML classes.
 * 
 * @author jpgravel
 *
 */
public interface XmlContent extends XmlBloc {

	/**
	 * Method transforming the current class into its XML string representation.
	 * 
	 * @return an XML string representation of itself.
	 */
	public String toXml();

	/**
	 * Method defining how the current class will write itself as XML to the
	 * given writer.
	 * 
	 * @param writer
	 *            Thw writer to write to.
	 * 
	 * @throws IOException
	 *             Thrown if an error occurs while writing to the writer.
	 */
	public void write(Writer writer) throws IOException;

	/**
	 * Gets the Id of the current node. This id has no representation in the
	 * given XML but is useful to uniquely identify each XML node within an XML
	 * graph.
	 * 
	 * @return The Id of the current XML content.
	 */
	public long getId();
}
