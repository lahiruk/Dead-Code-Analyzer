package lk.devfactory.store.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.models.DeadCode;
import lk.devfactory.store.Cache;

@Component
@Qualifier("deadCodeCache")
public class DeadCodeCacheStore implements Cache<String,DeadCode>{
	Map<String,DeadCode> cache = new ConcurrentHashMap<String,DeadCode>();
	
	public DeadCode getCacheEntry(String id) {
		return cache.get(id);
	}
	
	public void addCacheEntry(String id, DeadCode entity) {
		cache.put(id, entity);
	}
	
	public Stream<DeadCode> getAllEntries(){
		return cache.values().stream();
	}
}
