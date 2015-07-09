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

public class AllowedOperation extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = 6833818266102350733L;
	
	private Long assetTypeId = new Long(0);
	private Integer gracePeriod = new Integer(0);
	private Long id = new Long(0);
	private Long operationId = new Long(0);
	private Integer validPeriod = new Integer(0);
	
	private Operation operation;
	
	public AllowedOperation() {
		this.setOperation(null);
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#getAssetTypeId()
	 */
	
	public Long getAssetTypeId() {
		return this.assetTypeId;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#setAssetTypeId(java.lang.Long)
	 */
	
	public void setAssetTypeId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property assetTypeId cannot be null.");
		}
		this.notifyUpdate(this.assetTypeId, value);
		this.assetTypeId = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#getGracePeriod()
	 */
	
	public Integer getGracePeriod() {
		return this.gracePeriod;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#setGracePeriod(java.lang.Integer)
	 */
	
	public void setGracePeriod(Integer value) {
		if (value == null) {
			throw new NullPointerException("The property gracePeriod cannot be null.");
		}
		this.notifyUpdate(this.gracePeriod, value);
		this.gracePeriod = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#getId()
	 */
	
	public Long getId() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#setId(java.lang.Long)
	 */
	
	public void setId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property id cannot be null.");
		}
		this.notifyUpdate(this.id, value);
		this.id = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#getOperationId()
	 */
	
	public Long getOperationId() {
		return this.operationId;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#setOperationId(java.lang.Long)
	 */
	
	public void setOperationId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property operationId cannot be null.");
		}
		this.notifyUpdate(this.operationId, value);
		this.operationId = value;
	}

	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#getValidPeriod()
	 */
	
	public Integer getValidPeriod() {
		return this.validPeriod;
	}
	
	/* (non-Javadoc)
	 * @see com.girfid.rm.entities.database.AllowedOperationTable#setValidPeriod(java.lang.Integer)
	 */
	
	public void setValidPeriod(Integer value) {
		if (value == null) {
			throw new NullPointerException("The property validPeriod cannot be null.");
		}
		this.notifyUpdate(this.validPeriod, value);
		this.validPeriod = value;
	}

	
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof AllowedOperation)) {
			return false;
		}
		AllowedOperation obj = (AllowedOperation)o;
		return 
			Util.equals(this.assetTypeId, obj.assetTypeId) &&
			Util.equals(this.gracePeriod, obj.gracePeriod) &&
			Util.equals(this.id, obj.id) &&
			Util.equals(this.operationId, obj.operationId) &&
			Util.equals(this.validPeriod, obj.validPeriod);
	}
	
	
	public int hashCode() {
		return 2080806079 ^
			Util.hashCode(this.id);
	}
	
}