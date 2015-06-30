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
package org.formix.dsx.serialization.entities2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.formix.dsx.serialization.entities.KeyValuePair;

public class SelectionSet {

	private Set<KeyValuePair> all;
	private Set<KeyValuePair> excluded;
	private Set<KeyValuePair> included;

	public SelectionSet() {
		this.excluded = new HashSet<KeyValuePair>();
		this.included = new HashSet<KeyValuePair>();
		this.all = null;
	}

	public Set<KeyValuePair> getExcluded() {
		return excluded;
	}

	public void setExcluded(Set<KeyValuePair> all) {
		this.excluded = all;
	}

	public Set<KeyValuePair> getIncluded() {
		return included;
	}

	public void setIncluded(Set<KeyValuePair> included) {
		this.included = included;
	}

	@XmlTransient
	public Set<KeyValuePair> getAll() {
		if (this.all == null) {
			Set<KeyValuePair> all = new HashSet<KeyValuePair>();
			all.addAll(this.excluded);
			all.addAll(this.included);
			this.all = Collections.unmodifiableSet(all);
		}
		return this.all;
	}
}
