package lk.devfactory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalVariable extends Entity{
	
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

	}
	
	public GlobalVariable(String name, int lineNo, int columnNo) {
		this.name = name;
		this.lineNo = lineNo;
		this.columnNo = columnNo;
	}

}
