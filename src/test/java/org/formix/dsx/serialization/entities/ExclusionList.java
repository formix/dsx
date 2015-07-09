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

import java.util.List;

public class ExclusionList {
	private List<KeyValuePair> fullList;
	private List<KeyValuePair> excludedList;
	private List<KeyValuePair> includedList;
	
	public List<KeyValuePair> getFullList() {
		return fullList;
	}

	public void setFullList(List<KeyValuePair> fullList) {
		this.fullList = fullList;
	}

	public List<KeyValuePair> getExcludedList() {
		return excludedList;
	}

	public void setExcludedList(List<KeyValuePair> excludedList) {
		this.excludedList = excludedList;
	}

	public List<KeyValuePair> getIncludedList() {
		return includedList;
	}

	public void setIncludedList(List<KeyValuePair> includedList) {
		this.includedList = includedList;
	}	
}
