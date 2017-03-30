package lk.devfactory.store.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import lk.devfactory.store.Cache;

@Component
@Qualifier("gitUrlCache")
public class RepositoryIdentifierCacheStore implements Cache<String,UUID>{
	Map<String,UUID> cache = new ConcurrentHashMap<String,UUID>();
	Set<String> unique = Sets.newConcurrentHashSet();
	
	public UUID getCacheEntry(String url) {
		return cache.get(url);
	}
	
	public boolean addCacheEntry(String url, UUID uuid) {
		boolean added = unique.add(url);
		if (added) {
			cache.put(url, uuid);
		}
		return added;
	}
	
	public Stream<UUID> getAllEntries(){
		return cache.values().stream();
	}
}
