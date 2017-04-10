package lk.devfactory.model;

public abstract  class Entity {
	
	public abstract String getName();
	public abstract int getLineNo();
	public abstract int getColumnNo();

	@Override
	public String toString() {
		return getName().toString();
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return getName().equals(((Entity)obj).getName());
	}
}
