package lk.devfactory.store.impl;

public class UUID {
	
	private java.util.UUID UUID;
	
	public UUID() {
		this.UUID = java.util.UUID.randomUUID();
	}
	
	@Override
	public String toString() {
		return this.UUID.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.UUID.equals(((UUID)obj).UUID);
	}
	
	@Override
	public int hashCode() {
		return this.UUID.hashCode();
	}
}
