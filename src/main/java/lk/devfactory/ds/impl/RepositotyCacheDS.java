package lk.devfactory.ds.impl;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.store.Cache;
import lk.devfactory.store.impl.UUID;

@Component
@Qualifier("repositoryCacheDS")
public class RepositotyCacheDS implements RepositoryDS<UUID,Repository> {
	
	@Autowired()
	@Qualifier("repositoryCache")
	Cache<UUID, Repository> repositoryCache;

	@Autowired
	@Qualifier("gitUrlCache")
	Cache<String, UUID> gitUrlCache;

	@Override
	public void create(UUID id, Repository entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Repository findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Repository> findAll() {
		// TODO Auto-generated method stub
		return null;
	}


}
