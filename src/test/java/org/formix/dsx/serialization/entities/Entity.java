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

/**
 * Defines common methods for Entities. This interface is to be implemented if
 * the Bridge's save method is to be used.
 * 
 * @author Jean-Philippe Gravel
 * 
 */
public interface Entity {

	public abstract Long getId();
	public abstract void setId(Long id);
	
	/**
	 * Gets the current state of the object.
	 * 
	 * @return the current state of the object.
	 */
	public EntityState getEntityState();

	/**
	 * Sets the state of the current entity.
	 * 
	 * @param value
	 *            The state desired.
	 */
	public void setEntityState(EntityState value);

	/**
	 * Changes the state of the current object to DELETED. If the current object
	 * is NEW, it moves to the DEAD state.
	 */
	public void delete();

	/**
	 * Changes the state of the current object from:<br/>
	 * <br/>
	 * NEW, UPDATED to UNCHANGED;<br/>
	 * DELETED to DEAD;<br/>
	 * UNCHANGED to UNCHANGED (no change).<br/>
	 * <br/>
	 * Remark: It's the implementor responsibility to insure that an auto
	 * incremented surrogate key is set to an unreachable value when changing
	 * the state from DELETED to NEW. Zero or any negative values are suitable
	 * in most cases.
	 */
	public void apply();
}
