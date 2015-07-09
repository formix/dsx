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

import java.io.Serializable;
import java.sql.Timestamp;

public class Asset implements Serializable {

	private static final long serialVersionUID = 6660978854121043031L;
	public static final int INTERNALNUMBER1_SIZE = 50;
	public static final int INTERNALNUMBER2_SIZE = 50;
	public static final int NOTES_SIZE = 5592405;
	public static final int SERIALNUMBER_SIZE = 50;

	private Long assetTypeId = new Long(0);
	private Timestamp creationDate = Timestamp.valueOf("1970-01-01 00:00:00");
	private Long id = new Long(0);
	private String internalNumber1 = null;
	private String internalNumber2 = null;
	private Timestamp manufacturingDate = null;
	private String notes = null;
	private String serialNumber = null;
	private Long siteId = new Long(0);

	public Asset() {
	}

	public Long getAssetTypeId() {
		return this.assetTypeId;
	}

	public void setAssetTypeId(Long value) {
		if (value == null) {
			throw new NullPointerException(
					"The property assetTypeId cannot be null.");
		}
		this.assetTypeId = value;
	}

	public Timestamp getCreationDate() {
		return (Timestamp) this.creationDate.clone();
	}

	public void setCreationDate(Timestamp value) {
		if (value == null) {
			throw new NullPointerException(
					"The property creationDate cannot be null.");
		}
		this.creationDate = (Timestamp) value.clone();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property id cannot be null.");
		}
		this.id = value;
	}

	public String getInternalNumber1() {
		return this.internalNumber1;
	}

	public void setInternalNumber1(String value) {
		if (value.length() > INTERNALNUMBER1_SIZE) {
			System.err
					.println("Warning: The length of internalNumber1 exceeds INTERNALNUMBER1_SIZE.");
		}
		this.internalNumber1 = value;
	}

	public String getInternalNumber2() {
		return this.internalNumber2;
	}

	public void setInternalNumber2(String value) {
		if (value.length() > INTERNALNUMBER2_SIZE) {
			System.err
					.println("Warning: The length of internalNumber2 exceeds INTERNALNUMBER2_SIZE.");
		}
		this.internalNumber2 = value;
	}

	public Timestamp getManufacturingDate() {
		if (this.manufacturingDate == null)
			return null;
		return (Timestamp) this.manufacturingDate.clone();
	}

	public void setManufacturingDate(Timestamp value) {
		if (value == null)
			this.manufacturingDate = null;
		else
			this.manufacturingDate = (Timestamp) value.clone();
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String value) {
		if (value.length() > NOTES_SIZE) {
			System.err
					.println("Warning: The length of notes exceeds NOTES_SIZE.");
		}
		this.notes = value;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String value) {
		if (value.length() > SERIALNUMBER_SIZE) {
			System.err
					.println("Warning: The length of serialNumber exceeds SERIALNUMBER_SIZE.");
		}
		this.serialNumber = value;
	}

	public Long getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Long value) {
		if (value == null) {
			throw new NullPointerException(
					"The property siteId cannot be null.");
		}
		this.siteId = value;
	}

}