package lk.devfactory.store.impl;

//TODO add javadocs
public class UUIDGenerator {
  
  public static final UUID get(){
    //generate random UUIDs
    return new UUID();
  }
 
  public static final UUID get(String id){
	    return new UUID(id);
  }
} 
