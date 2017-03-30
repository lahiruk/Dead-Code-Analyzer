package lk.devfactory.store.impl;

public class UUID {
	
	private String UUID;
	
	public UUID() {
		this.UUID = java.util.UUID.randomUUID().toString();
	}
	
	protected UUID(String id) {
		UUID = id;
	}
	
	@Override
	public String toString() {
		return this.UUID;
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
