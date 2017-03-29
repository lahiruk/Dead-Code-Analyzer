package lk.devfactory.store.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;

@Component
@Qualifier("deadCodeCache")
public class DeadCodeCacheStore implements Cache<UUID,DeadCode>{
	Map<UUID,DeadCode> cache = new ConcurrentHashMap<UUID,DeadCode>();
	
	public DeadCode getCacheEntry(UUID id) {
		return cache.get(id);
	}
	
	public void addCacheEntry(UUID id, DeadCode deadCode) {
		cache.put(id, deadCode);
	}
	
	public Stream<DeadCode> getAllEntries(){
		return cache.values().stream();
	}
}
