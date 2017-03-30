package lk.devfactory.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Function extends Entity{
	
	String name;
	
	int lineNo;
	
	int columnNo;
	
	List<FunctionParameter> parameters;
	
	List<LocalVariable> variables;

    @JsonProperty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @JsonProperty
	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

    @JsonProperty
	public int getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
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

	public Function() {
		this.parameters = new ArrayList<FunctionParameter>();
		this.variables = new ArrayList<LocalVariable>();
	}

	public Function(String name, int lineNo, int columnNo, List<FunctionParameter> parameters, List<LocalVariable> variables) {
		super();
		this.name = name;
		this.lineNo = lineNo;
		this.columnNo = columnNo;
		this.parameters = parameters;
		this.variables = variables;
	}

}
