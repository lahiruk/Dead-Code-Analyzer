package lk.devfactory.repository;

import lk.devfactory.models.Repository;
import lk.devfactory.store.impl.UUID;

public interface DeadCodeDO {
	public void analyse(UUID id, Repository repository);
}
