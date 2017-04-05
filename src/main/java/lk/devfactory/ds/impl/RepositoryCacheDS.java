package lk.devfactory.ds.impl;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;
import lk.devfactory.store.impl.UUID;

//TODO Add javadocs
@Component
@Qualifier("repositoryCacheDS")
public class RepositoryCacheDS implements RepositoryDS<UUID,Repository,String> {
	
	@Autowired()
	@Qualifier("repositoryCache")
	Cache<UUID, Repository> repositoryCache;

	@Autowired
	@Qualifier("gitUrlCache")
	Cache<String, UUID> gitUrlCache;

	@Override
	public boolean create(UUID id, Repository entity) {
		boolean added = false;
		UUID cacheId =gitUrlCache.getCacheEntry(entity.getUrl());
		if (cacheId == null ){
			added = gitUrlCache.addCacheEntry(entity.getUrl(), id);
			if (added) {
				repositoryCache.addCacheEntry(id, entity);
			}
		}
		return added;
	}

	@Override
	public Repository findById(UUID id) {
		return repositoryCache.getCacheEntry(id);
	}

	@Override
	public Stream<Repository> findAll() {
		return repositoryCache.getAllEntries();
	}

	@Override
	public void remove(UUID id) {
		Repository repository = repositoryCache.getCacheEntry(id);
		gitUrlCache.removeCacheEntry(repository.getUrl());
		repositoryCache.removeCacheEntry(id);
	}

	@Override
	public Repository findByNonIdUniqueKey(String unique) {
		UUID uuid = gitUrlCache.getCacheEntry(unique);
		return repositoryCache.getCacheEntry(uuid);
	}

	@Override
	public void update(UUID id, Repository Entiry) {
		repositoryCache.removeCacheEntry(id);
		repositoryCache.addCacheEntry(id, Entiry);
	}


}
