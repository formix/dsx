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

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;




public class OwnerSiteBundle {

	private SortedSet<Owner> owners;
	private SortedSet<Site> sites;
	
	public OwnerSiteBundle() {
		this.owners = new TreeSet<Owner>(new Comparator<Owner>() {
			public int compare(Owner o1, Owner o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		this.sites = new TreeSet<Site>(new Comparator<Site>() {
			public int compare(Site o1, Site o2) {
				int ret = 0;
				if (ret == 0)
					ret = (int)(o1.getOwnerId() - o2.getOwnerId());
				if (ret == 0)
					ret = o1.getName().compareTo(o2.getName());
				return ret;
			}
		});
	}

	
	public OwnerSiteBundle(Collection<Owner> owners, Collection<Site> sites) {
		this();
		this.owners.addAll(owners);
		this.sites.addAll(sites);
	}

	public SortedSet<Owner> getOwners() {
		return owners;
	}

	public void setOwners(SortedSet<Owner> owners) {
		this.owners = owners;
	}

	public SortedSet<Site> getSites() {
		return sites;
	}

	public void setSites(SortedSet<Site> sites) {
		this.sites = sites;
	}
}
