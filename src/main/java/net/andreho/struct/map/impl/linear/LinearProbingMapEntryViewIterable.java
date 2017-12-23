package net.andreho.struct.map.impl.linear;

import net.andreho.struct.map.MutableEntry;

import java.util.Iterator;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:53.
 */
final class LinearProbingMapEntryViewIterable<K, V> implements Iterable<MutableEntry<K, V>> {
   final LinearProbingHashMap<K, V> map;
   final boolean asView;

   public LinearProbingMapEntryViewIterable(LinearProbingHashMap<K, V> map, boolean asView) {
      this.map = map;
      this.asView = asView;
   }

   @Override
   public Iterator<MutableEntry<K, V>> iterator() {
      return new LinearProbingMapEntryViewIterator<>(this.map, this.asView);
   }
}
