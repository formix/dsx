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



public class Operation extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = -2550620593714857966L;
	public static final int NAME_SIZE = 50;
	
	private Long id = new Long(0);
	private Boolean isQualifiable = new Boolean(false);
	private String name = "";
	private Boolean isVisible;
	
	public Operation() {}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#getId()
	 */
	
	public Long getId() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#setId(java.lang.Long)
	 */
	
	public void setId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property id cannot be null.");
		}
		this.notifyUpdate(this.id, value);
		this.id = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#getName()
	 */
	
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#setName(java.lang.String)
	 */
	
	public void setName(String value) {
		if (value == null) {
			throw new NullPointerException("The property name cannot be null.");
		}
		if ((value != null) && (value.length() > NAME_SIZE)) {
			System.err.println("Warning: The length of name exceeds NAME_SIZE.");
		}
		this.notifyUpdate(this.name, value);
		this.name = value;
	}

	
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof Operation)) {
			return false;
		}
		Operation obj = (Operation)o;
		return 
			Util.equals(this.id, obj.id) &&
			Util.equals(this.name, obj.name);
	}
	
	
	public int hashCode() {
		return -628296377 ^
				Util.hashCode(this.id) ^
				Util.hashCode(this.name);
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#getIsQualifiable()
	 */
	
	public Boolean getIsQualifiable() {
		return isQualifiable;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.OperationTable#setIsQualifiable(java.lang.Boolean)
	 */
	
	public void setIsQualifiable(Boolean isQualifiable) {
		this.isQualifiable = isQualifiable;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}
	
}