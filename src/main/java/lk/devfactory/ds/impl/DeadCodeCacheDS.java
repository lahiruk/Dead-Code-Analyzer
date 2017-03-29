package lk.devfactory.ds.impl;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.store.Cache;

//TODO Remove testing setter
@Component
@Qualifier("deadCodeCacheDS")
public class DeadCodeCacheDS implements RepositoryDS<String,DeadCode> {
	
	@Autowired()
	@Qualifier("deadCodeCache")
	Cache<String, DeadCode> deadCodeCache;
	
	public void setDeadCodeCache(Cache<String, DeadCode> deadCodeCache) {
		this.deadCodeCache = deadCodeCache;
	}

	public void create(String id, DeadCode entity){
		deadCodeCache.addCacheEntry(id, entity);
	}

	@Override
	public DeadCode findById(String id) {
		return deadCodeCache.getCacheEntry(id);
	}

	@Override
	public Stream<DeadCode> findAll() {
		return deadCodeCache.getAllEntries();
	}

}
