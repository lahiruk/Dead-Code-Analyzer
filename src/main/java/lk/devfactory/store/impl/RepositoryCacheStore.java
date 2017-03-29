package lk.devfactory.store.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;

@Component
@Qualifier("repositoryCache")
public class RepositoryCacheStore implements Cache<UUID,Repository>{
	Map<UUID,Repository> cache = new ConcurrentHashMap<UUID,Repository>();
	
	public Repository getCacheEntry(UUID id) {
		return cache.get(id);
	}
	
	public void addCacheEntry(UUID id, Repository repository) {
		cache.put(id, repository);
	}
	
	public Stream<Repository> getAllEntries(){
		return cache.values().stream();
	}
}
