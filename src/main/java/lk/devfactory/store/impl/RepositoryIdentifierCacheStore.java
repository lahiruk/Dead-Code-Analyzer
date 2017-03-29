package lk.devfactory.store.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.store.Cache;

@Component
@Qualifier("gitUrlCache")
public class RepositoryIdentifierCacheStore implements Cache<String,UUID>{
	Map<String,UUID> cache = new ConcurrentHashMap<String,UUID>();
	
	public UUID getCacheEntry(String url) {
		return cache.get(url);
	}
	
	public void addCacheEntry(String url, UUID uuid) {
		cache.put(url, uuid);
	}
	
	public Stream<UUID> getAllEntries(){
		return cache.values().stream();
	}
}
