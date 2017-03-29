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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DeadCode{

    private String className;
    
    private List<GlobalVariable> globalVariables;
    
    private List<Function> functions;

    @JsonProperty
    public String getClassName() {
        return className;
    }

    public void setClassName(String name) {
        this.className = name;
    }

    @JsonProperty
	public List<GlobalVariable> getGlobalVariables() {
		return globalVariables;
	}

	public void setGlobalVariables(List<GlobalVariable> globalVariables) {
		this.globalVariables = globalVariables;
	}

    @JsonProperty
	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	public DeadCode() {
		this.className ="lk.devfactory.Test.class";
		this.globalVariables = new ArrayList<GlobalVariable>();
		this.globalVariables.add(new GlobalVariable());
		this.functions = new ArrayList<Function>();
		this.functions.add(new Function());
    }

	public DeadCode(String className, List<GlobalVariable> globalVariables, List<Function> functions) {
		super();
		this.className = className;
		this.globalVariables = globalVariables;
		this.functions = functions;
	}

}