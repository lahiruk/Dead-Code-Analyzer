package lk.devfactory.ds;

import java.util.stream.Stream;

public interface RepositoryDS<K,E,U> {
	public boolean create(K id, E entity);
	public E findById(K id);
	public Stream<E> findAll();
	public void remove(K id);
	public E findByNonIdUniqueKey(U unique);
	public void update(K id, E Entiry);
}
