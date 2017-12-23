package net.andreho.struct.map.impl.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:55.
 */
final class LinearProbingMapValueCollectionIterator<V> implements Iterator<V> {
   private final int[] hashes;
   private final Object[] values;
   private int index;

   LinearProbingMapValueCollectionIterator(final int[] hashes, final Object[] values) {
      this.hashes = hashes;
      this.values = values;
      this.index = LinearProbingHashMap.findNext(hashes, 0, 0);
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
      V value = (V) this.values[index];
      this.index = LinearProbingHashMap.findNext(this.hashes, index, 0);
      return value;
   }
}
