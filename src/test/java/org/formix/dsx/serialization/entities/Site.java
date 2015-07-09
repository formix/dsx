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

public class Site extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = 6750468933310556382L;
	public static final int ADDRESS1_SIZE = 50;
	public static final int ADDRESS2_SIZE = 50;
	public static final int CITY_SIZE = 50;
	public static final int COUNTRY_SIZE = 50;
	public static final int DESCRIPTION_SIZE = 5592405;
	public static final int NAME_SIZE = 50;
	public static final int STATE_SIZE = 50;
	public static final int POSTALCODE_SIZE = 20;
	
	private String address1 = null;
	private String address2 = null;
	private String city = null;
	private String country = null;
	private String description = null;
	private Long id = new Long(0);
	private String name = "";
	private Long ownerId = new Long(0);
	private String postalCode = "";
	private String state = null;
	
	public Site() {}

	public String getAddress1() {
		return this.address1;
	}
	
	public void setAddress1(String value) {
		if (value.length() > ADDRESS1_SIZE) {
			System.err.println("Warning: The length of address1 exceeds ADDRESS1_SIZE.");
		}
		this.notifyUpdate();
		this.address1 = value;
	}

	public String getAddress2() {
		return this.address2;
	}
	
	public void setAddress2(String value) {
		if (value.length() > ADDRESS2_SIZE) {
			System.err.println("Warning: The length of address2 exceeds ADDRESS2_SIZE.");
		}
		this.notifyUpdate();
		this.address2 = value;
	}

	public String getCity() {
		return this.city;
	}
	
	public void setCity(String value) {
		if (value.length() > CITY_SIZE) {
			System.err.println("Warning: The length of city exceeds CITY_SIZE.");
		}
		this.notifyUpdate();
		this.city = value;
	}

	public String getCountry() {
		return this.country;
	}
	
	public void setCountry(String value) {
		if (value.length() > COUNTRY_SIZE) {
			System.err.println("Warning: The length of country exceeds COUNTRY_SIZE.");
		}
		this.notifyUpdate();
		this.country = value;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String value) {
		if (value.length() > DESCRIPTION_SIZE) {
			System.err.println("Warning: The length of description exceeds DESCRIPTION_SIZE.");
		}
		this.notifyUpdate();
		this.description = value;
	}

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

	public Long getOwnerId() {
		return this.ownerId;
	}
	
	public void setOwnerId(Long value) {
		if (value == null) {
			throw new NullPointerException("The property ownerId cannot be null.");
		}
		this.notifyUpdate();
		this.ownerId = value;
	}

	public String getPostalCode() {
		return this.postalCode;
	}
	
	public void setPostalCode(String value) {
		if (value.length() > POSTALCODE_SIZE) {
			System.err.println("Warning: The length of postalCode exceeds POSTALCODE_SIZE.");
		}
		this.notifyUpdate();
		this.postalCode = value;
	}

	public String getState() {
		return this.state;
	}
	
	public void setState(String value) {
		if (value.length() > STATE_SIZE) {
			System.err.println("Warning: The length of state exceeds STATE_SIZE.");
		}
		this.notifyUpdate();
		this.state = value;
	}

	@Override
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof Site)) {
			return false;
		}
		Site obj = (Site)o;
		return 
			Util.equals(this.address1, obj.address1) &&
			Util.equals(this.address2, obj.address2) &&
			Util.equals(this.city, obj.city) &&
			Util.equals(this.country, obj.country) &&
			Util.equals(this.description, obj.description) &&
			Util.equals(this.id, obj.id) &&
			Util.equals(this.name, obj.name) &&
			Util.equals(this.ownerId, obj.ownerId) &&
			Util.equals(this.state, obj.state);
	}
	
	@Override
	public int hashCode() {
		return 2577255 ^
			Util.hashCode(this.address1) ^
			Util.hashCode(this.address2) ^
			Util.hashCode(this.city) ^
			Util.hashCode(this.country) ^
			Util.hashCode(this.description) ^
			Util.hashCode(this.id) ^
			Util.hashCode(this.name) ^
			Util.hashCode(this.ownerId) ^
			Util.hashCode(this.state);
	}
	
}