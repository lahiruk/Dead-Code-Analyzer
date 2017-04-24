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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Clazz extends Entity{

    private String name;
    
    private List<GlobalVariable> globalVariables;
    
    private List<Function> functions;
    
    private List<FunctionParameter> parameters;
    
    private List<LocalVariable> variables;

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
	

    @JsonProperty
    public List<FunctionParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<FunctionParameter> parameters) {
        this.parameters = parameters;
    }

    @JsonProperty
    public List<LocalVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<LocalVariable> variables) {
        this.variables = variables;
    }

	public Clazz() {
		this.globalVariables = new ArrayList<GlobalVariable>();
		this.functions = new ArrayList<Function>();
	    this.parameters = new ArrayList<FunctionParameter>();
	    this.variables = new ArrayList<LocalVariable>();
    }

	public Clazz(String name, List<GlobalVariable> globalVariables, List<Function> functions, 
	          List<FunctionParameter> parameters, List<LocalVariable> variables) {
		super();
		this.name = name;
		this.globalVariables = globalVariables;
		this.functions = functions;
	    this.parameters = parameters;
	    this.variables = variables;
	}

}