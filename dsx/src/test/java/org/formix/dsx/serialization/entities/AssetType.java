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
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

public class AssetType extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = -8750644337662076474L;
	public static final int NAME_SIZE = 50;
	public static final int PICTURE_SIZE = 256;

	private Long id = new Long(0);
	//private Long managerId = new Long(0);
	private String name = "";
	private Integer objectClass = new Integer(0);
	private String picture = null;
	private Integer accessPassword = null;
	
	private List<AllowedOperation> allowedOperations;
	
	public AssetType() {
		this.allowedOperations = null;
	}

	public List<AllowedOperation> getAllowedOperations() {
		return allowedOperations;
	}

	public void setAllowedOperations(List<AllowedOperation> allowedOperations) {
		this.allowedOperations = allowedOperations;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#getId()
	 */
	
	public Long getId() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#setId(java.lang.Long)
	 */
	
	public void setId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property id cannot be null.");
		}
		this.notifyUpdate(this.id, value);
		this.id = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#getName()
	 */
	
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#setName(java.lang.String)
	 */
	
	public void setName(String value) {
		if (value == null) {
			value = "";
		}
		if ((value != null) && (value.length() > NAME_SIZE)) {
			System.err.println("Warning: The length of name exceeds NAME_SIZE.");
		}
		this.notifyUpdate(this.name, value);
		this.name = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#getObjectClass()
	 */
	
	public Integer getObjectClass() {
		return this.objectClass;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#setObjectClass(java.lang.Integer)
	 */
	
	public void setObjectClass(Integer value) {
		if (value == null) {
			throw new NullPointerException("The property objectClass cannot be null.");
		}
		this.notifyUpdate(this.objectClass, value);
		this.objectClass = value;
	}



	
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof AssetType)) {
			return false;
		}
		AssetType obj = (AssetType)o;
		return 
			Util.equals(this.id, obj.id) &&
			//Util.equals(this.managerId, obj.managerId) &&
			Util.equals(this.name, obj.name) &&
			//Util.equals(this.nextTagSerial, obj.nextTagSerial) &&
			Util.equals(this.objectClass, obj.objectClass);
	}
	
	
	public int hashCode() {
		return -975694294 ^
				Util.hashCode(this.id) ^
				//Util.hashCode(this.managerId) ^
				Util.hashCode(this.name) ^
				//Util.hashCode(this.nextTagSerial) ^
				Util.hashCode(this.objectClass);
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#getPicture()
	 */
	
	public String getPicture() {
		return picture;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#setPicture(java.lang.String)
	 */
	
	public void setPicture(String value) {
		if ((value != null) && (value.length() > PICTURE_SIZE)) {
			System.err.println("Warning: The length of name exceeds PICTURE_SIZE.");
		}
		this.notifyUpdate(this.picture, value);
		this.picture = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#getAccessPassword()
	 */
	
	public Integer getAccessPassword() {
		return accessPassword;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AssetTypeTable#setAccessPassword(java.lang.Integer)
	 */
	
	public void setAccessPassword(Integer value) {
		this.notifyUpdate(this.accessPassword, value);
		this.accessPassword = value;
	}

	@XmlTransient
	public AllowedOperation getAllowedOperation(Long operationId) {
		if (this.allowedOperations == null)
			return null;
		
		for (AllowedOperation ao : this.allowedOperations) {
			if (ao.getOperationId().equals(operationId)) {
				return ao;
			}
		}
		return null;
	}


	
	public String toString() {
		return this.name;
	}


}