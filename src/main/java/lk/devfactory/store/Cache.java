package lk.devfactory.store;

import java.util.stream.Stream;

public interface Cache<K,E> {
	public E getCacheEntry(K id);
	
	public void addCacheEntry(K id, E entry);
	
	public Stream<E> getAllEntries();
}
