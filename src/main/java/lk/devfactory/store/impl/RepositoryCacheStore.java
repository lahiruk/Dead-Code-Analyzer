package lk.devfactory.store.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.model.Repository;
import lk.devfactory.store.Cache;

//TODO Add javadocs
@Component
@Qualifier("repositoryCache")
public class RepositoryCacheStore implements Cache<UUID,Repository>{
	Map<UUID,Repository> cache = new ConcurrentHashMap<UUID,Repository>();
	
	public Repository getCacheEntry(UUID id) {
		return cache.get(id);
	}
	
	public boolean addCacheEntry(UUID id, Repository repository) {
		cache.put(id, repository);
		return true;
	}
	
	public void removeCacheEntry(UUID id) {
		cache.remove(id);
	}
	
	public Stream<Repository> getAllEntries(){
		return cache.values().stream();
	}
}
