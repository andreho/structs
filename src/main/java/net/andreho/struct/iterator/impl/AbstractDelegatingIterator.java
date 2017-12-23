package net.andreho.struct.iterator.impl;

import java.util.Iterator;
import java.util.Objects;

/**
 * Created by a.hofmann on 07.07.2016.
 */
public abstract class AbstractDelegatingIterator<E, V>
    implements Iterator<E> {

  protected final Iterator<V> iterator;

  public AbstractDelegatingIterator(final Iterator<V> iterator) {
    this.iterator = Objects.requireNonNull(iterator);
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public E next() {
    return retrieveValue(iterator.next());
  }

  protected E retrieveValue(V value) {
    return (E) value;
  }
}
