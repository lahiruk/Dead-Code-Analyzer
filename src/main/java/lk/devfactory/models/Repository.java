/*
 *  Copyright 2016 SmartBear Software
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package lk.devfactory.models;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;


public class Repository {
    private String id;
    private String url;
    private List<DeadCode> deadCode;
    private String status;
    private OffsetDateTime created = OffsetDateTime.now();

    @JsonProperty
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }

    public List<DeadCode> getDeadCode() {
		return deadCode;
	}

	public void setDeadCode(List<DeadCode> deadCode) {
		this.deadCode = deadCode;
	}

	@JsonProperty("status")
    @ApiModelProperty(value = "Repository status in the store", allowableValues = "pending,downloading,analysing,completed,failed")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
    	return getUrl() + "-->" +getStatus();
    }
}
