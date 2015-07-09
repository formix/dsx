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

import javax.xml.bind.annotation.XmlTransient;


public abstract class EntityBase implements Entity {

	private EntityState entityState;
	private boolean listeningForChanges;

	public EntityBase() {
		this.entityState = EntityState.NEW;
		this.listeningForChanges = true;
	}

	@Override
	public EntityState getEntityState() {
		return this.entityState;
	}

	@Override
	public void setEntityState(EntityState value) {
		this.entityState = value;
	}

	@Override
	public void delete() {
		if (!this.listeningForChanges)
			return;

		if (this.entityState == EntityState.NEW)
			this.entityState = EntityState.DEAD;
		else
			this.entityState = EntityState.DELETED;
	}

	@Override
	public void apply() {
		if (!this.listeningForChanges)
			return;

		if (this.entityState == EntityState.DELETED)
			this.entityState = EntityState.DEAD;
		else
			this.entityState = EntityState.UNCHANGED;
	}

	protected void notifyUpdate(Object oldValue, Object newValue) {
		if (!this.listeningForChanges)
			return;

		if ((oldValue == null) && (newValue == null))
			return;
		
		if ((oldValue != null) && (newValue == null)) {
			this.notifyUpdate();
			return;
		}

		if ((oldValue == null) && (newValue != null)) {
			this.notifyUpdate();
			return;
		}
		
		if (!oldValue.equals(newValue))
			this.notifyUpdate();
	}
	
	protected void notifyUpdate() {
		if (this.entityState == EntityState.UNCHANGED) {
			this.entityState = EntityState.UPDATED;
		}
	}

	/**
	 * Tells if the current entity is listening for changes. If the entity is
	 * not listening for changes, nothing is going to affect the entityState
	 * outside of setEntityState.
	 * 
	 * @return True if the current entity is listening for changes. False
	 *         otherwise.
	 */
	@XmlTransient
	public boolean isListenForChanges() {
		return listeningForChanges;
	}

	/**
	 * Tells the current entity to start listening for changes.
	 */
	public void startListeningForChanges() {
		this.listeningForChanges = true;
	}

	/**
	 * Tells the current entity to stop listening for changes.
	 */
	public void stopListeningForChanges() {
		this.listeningForChanges = false;
	}

}
