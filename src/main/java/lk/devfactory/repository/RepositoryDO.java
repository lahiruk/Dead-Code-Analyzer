package lk.devfactory.repository;

import java.util.stream.Stream;

import lk.devfactory.models.Repository;
import lk.devfactory.store.impl.UUID;

public interface RepositoryDO {
	public Repository add(UUID id, Repository repository);
	public Stream<Repository> findAll();
	public Stream<Repository> findById(UUID id);
}
