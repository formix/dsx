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
 * Describes the different states that an Entity may take.
 * 
 * @author Jean-Philippe Gravel
 * 
 */
public enum EntityState {
	/**
	 * The entity have been created but does not exists in the database.
	 */
	NEW,

	/**
	 * The entity is in the exact state as when it was loaded.
	 */
	UNCHANGED,

	/**
	 * One or more of the entity properties have been changed.
	 */
	UPDATED,

	/**
	 * The entity have been deleted but the deletion is not yet effective in the
	 * database.
	 */
	DELETED,

	/**
	 * A NEW entity on which the delete() method have been called before being
	 * saved in the database switch to the DEAD state. This is also the case
	 * after a DELETED entity is saved (deleted) in the database, which happens
	 * after a save() call.
	 */
	DEAD
}