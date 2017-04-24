package lk.devfactory.repository;

import java.util.List;

import lk.devfactory.model.Clazz; 

public interface DeadCodeDO {
	public List<Clazz> analyse(String repoId);
}
