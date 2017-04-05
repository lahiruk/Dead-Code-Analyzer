package lk.devfactory.repository;

//TODO Add javadocs
import java.util.stream.Stream;

import lk.devfactory.models.Repository;
import lk.devfactory.store.impl.UUID;

public interface RepositoryDO {
	public Repository add(UUID id, Repository repository);
	public Stream<Repository> findAll();
	public Repository findById(UUID id);
	public void remove(UUID id);
}
