package lk.devfactory.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocalVariable {
	
	String name;
	
	int lineNo;
	
	int columnNo;

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

	public LocalVariable() {
		this.name = "LocalVar1";
		this.lineNo = 10;
		this.columnNo = 15;
	}
	
	public LocalVariable(String name, int lineNo, int columnNo) {
		this.name = name;
		this.lineNo = lineNo;
		this.columnNo = columnNo;
	}

}
