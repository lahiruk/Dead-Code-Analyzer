package lk.devfactory.ds;

import java.util.stream.Stream;

public interface RepositoryDS<K,E> {
	public void create(K id, E entity);
	public E findById(K id);
	public Stream<E> findAll();
}
