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

package lk.devfactory.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Repository extends RepositoryBase{
    private String id;
    private List<Clazz> clazz;
    private String status;
    private OffsetDateTime created = OffsetDateTime.now();
    private OffsetDateTime preparedToAnalyseAt;
    private OffsetDateTime completedAt;
    private String result;
    private boolean existing = false;

    @JsonIgnore
    public boolean isExisting() {
      return existing;
    }

    public void setExisting(boolean existing) {
      this.existing = existing;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    public OffsetDateTime getPreparedToAnalyseAt() {
		return preparedToAnalyseAt;
	}

	public void setPreparedToAnalyseAt(OffsetDateTime preparedOn) {
		this.preparedToAnalyseAt = preparedOn;
	}

	@JsonProperty
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(OffsetDateTime completedOn) {
		this.completedAt = completedOn;
	}

	@JsonProperty
	public String getResult() {
      return result;
    }

    public void setResult(String message) {
      this.result = message;
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
        return super.getUrl();
    }

    public void setUrl(String name) {
        super.setUrl(name);
    }

    @JsonProperty
    public List<Clazz> getClasses() {
		return clazz;
	}

	public void setClasses(List<Clazz> clazz) {
		this.clazz = clazz;
	}

	@JsonProperty
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
