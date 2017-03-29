package lk.devfactory.ds.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.store.Cache;
import lk.devfactory.store.impl.UUID;

@Component
@Qualifier("deadCodeCacheDS")
public class DeadCodeCacheDS implements RepositoryDS {
	
	@Autowired()
	@Qualifier("deadCodeCache")
	Cache<UUID, DeadCode> deadCodeCache;

}
