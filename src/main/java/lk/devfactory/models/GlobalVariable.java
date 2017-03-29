package lk.devfactory.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalVariable {
	
	@JsonProperty
	String name;
	
	@JsonProperty
	int lineNo;
	
	@JsonProperty
	int columnNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	public GlobalVariable() {
		this.name = "Global1";
		this.lineNo = 10;
		this.columnNo = 15;
	}
	
	public GlobalVariable(String name, int lineNo, int columnNo) {
		this.name = name;
		this.lineNo = lineNo;
		this.columnNo = columnNo;
	}

}
