package lk.devfactory.ds.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;
import lk.devfactory.store.impl.UUID;

@Component
@Qualifier("repositoryCacheDS")
public class RepositotyCacheDS implements RepositoryDS {
	
	@Autowired()
	@Qualifier("repositoryCache")
	Cache<UUID, Repository> repositoryCache;

	@Autowired
	@Qualifier("gitUrlCache")
	Cache<String, UUID> gitUrlCache;
}
