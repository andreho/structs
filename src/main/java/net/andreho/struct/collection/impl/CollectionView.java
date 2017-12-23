package net.andreho.struct.collection.impl;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * <br/>Created by a.hofmann on 08.04.2017 at 09:36.
 */
public class CollectionView<E,V> extends AbstractCollection<V> {
   private final Function<E,V> function;
   private final Collection<E> collection;

   public CollectionView(final Collection<E> collection, final Function<E, V> function) {
      this.function = Objects.requireNonNull(function);
      this.collection = Objects.requireNonNull(collection);
   }

   @Override
   public Iterator<V> iterator() {
      final Iterator<E> iterator = collection.iterator();
      return new Iterator<V>() {
         @Override
         public boolean hasNext() {
            return iterator.hasNext();
         }

         @Override
         public V next() {
            return function.apply(iterator.next());
         }
      };
   }

   @Override
   public int size() {
      return collection.size();
   }
}
