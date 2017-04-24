package lk.devfactory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Function extends Entity implements EntityLocation {
	
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

	public Function() {
    }
	
	public Function(String name, int lineNo, int columnNo) {
		super();
		this.name = name;
		this.lineNo = lineNo;
		this.columnNo = columnNo;
	}

}
