package lk.devfactory.ds;

import java.util.stream.Stream;

public interface DeadCodeDS<K,E> {
	public boolean create(K id, E entity);
	public E findById(K id);
	public Stream<E> findAll();
	public void remove(K id);
}
