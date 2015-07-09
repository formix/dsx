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
package org.formix.dsx.serialization.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="owner")
public class ExtendedOwner extends Owner {
	private static final long serialVersionUID = 4133940363735280948L;

	private String extension;
	
	public ExtendedOwner() {
		this.extension = "nothing verry special about this...";
	}
	
	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String value) {
		this.extension = value;
	}

}
