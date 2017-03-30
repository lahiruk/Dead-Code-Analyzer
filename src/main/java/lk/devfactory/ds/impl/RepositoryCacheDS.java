package lk.devfactory.ds.impl;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;
import lk.devfactory.store.impl.UUID;

//TODO Remove testing setter
@Component
@Qualifier("repositoryCacheDS")
public class RepositoryCacheDS implements RepositoryDS<UUID,Repository> {
	
	@Autowired()
	@Qualifier("repositoryCache")
	Cache<UUID, Repository> repositoryCache;

	public void setRepositoryCache(Cache<UUID, Repository> repositoryCache) {
		this.repositoryCache = repositoryCache;
	}

	@Autowired
	@Qualifier("gitUrlCache")
	Cache<String, UUID> gitUrlCache;

	public void setGitUrlCache(Cache<String, UUID> gitUrlCache) {
		this.gitUrlCache = gitUrlCache;
	}

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


}
