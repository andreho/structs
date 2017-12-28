package net.andreho.struct.map.impl.linear;

import net.andreho.struct.collection.impl.AbstractMutableCollection;

import java.util.Iterator;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:55.
 */
final class LinearProbingMapValueCollection<V> extends AbstractMutableCollection<V> {
   private final LinearProbingHashMap<?, V> map;

   LinearProbingMapValueCollection(LinearProbingHashMap<?, V> immutableEntries) {
      super();
      this.map = immutableEntries;
   }

   @Override
   public Iterator<V> iterator() {
      return new LinearProbingMapValueCollectionIterator<>(map);
   }

   @Override
   public int size() {
      return map.size();
   }
}
