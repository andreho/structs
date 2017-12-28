package net.andreho.struct.map.impl.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:55.
 */
final class LinearProbingMapValueCollectionIterator<V> implements Iterator<V> {
   private final LinearProbingHashMap<?, V> map;
   private int index;

   LinearProbingMapValueCollectionIterator(final LinearProbingHashMap<?,V> map) {
      this.map = map;
      this.index = LinearProbingHashMap.findNext(map.hashes, 0, 0);
   }

   @Override
   public boolean hasNext() {
      return index != -1;
   }

   @Override
   public V next() {
      if (!hasNext()) {
         throw new NoSuchElementException();
      }
      int index = this.index;
      V value = (V) this.map.values[index];
      this.index = LinearProbingHashMap.findNext(this.map.hashes, index, 1);
      return value;
   }
}
