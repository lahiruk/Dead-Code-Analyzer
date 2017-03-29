package lk.devfactory.repository;

import java.util.List;

import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.store.impl.UUID;

public interface DeadCodeDO {
	public List<DeadCode> analyse(UUID id, Repository repository);
}
