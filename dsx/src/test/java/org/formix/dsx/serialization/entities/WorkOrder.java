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

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class WorkOrder {
	
	private Long deviceId;
	private List<Long> assetIds;
	private Timestamp dateSent;
	
	public WorkOrder() {
		this.deviceId = 0L;
		this.assetIds = new LinkedList<Long>();
	}

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public void setAssetIds(List<Long> assetIds) {
		this.assetIds = assetIds;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Timestamp getDateSent() {
		return dateSent;
	}

	public void setDateSent(Timestamp dateSent) {
		this.dateSent = dateSent;
	}
	
}
