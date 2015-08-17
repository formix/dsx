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

import java.io.Serializable;

public class Owner extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = 101460708305532774L;
	public static final int NAME_SIZE = 50;
	
	private Long id = new Long(0);
	private String name = "";
	
	public Owner() {}

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property id cannot be null.");
		}
		this.notifyUpdate();
		this.id = value;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String value) {
		if (value == null) {
			throw new NullPointerException("The property name cannot be null.");
		}
		if (value.length() > NAME_SIZE) {
			System.err.println("Warning: The length of name exceeds NAME_SIZE.");
		}
		this.notifyUpdate();
		this.name = value;
	}

	@Override
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof Owner)) {
			return false;
		}
		Owner obj = (Owner)o;
		return 
			Util.equals(this.id, obj.id) &&
			Util.equals(this.name, obj.name);
	}
	
	@Override
	public int hashCode() {
		return 76612243 ^
			Util.hashCode(this.id) ^
			Util.hashCode(this.name);
	}
	
}