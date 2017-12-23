package net.andreho.struct.map.impl.linear;

import net.andreho.struct.map.MutableEntry;
import net.andreho.struct.map.impl.MutableEntryImpl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:56.
 */
final class LinearProbingMapEntryViewIterator<K, V> implements Iterator<MutableEntry<K, V>>, MutableEntry<K, V> {
   final LinearProbingHashMap<K, V> map;

   private final boolean asView;
   private final int[] hashes;
   private final Object[] keys;
   private final Object[] values;
   private int prev = -1;
   private int index;

   LinearProbingMapEntryViewIterator(final LinearProbingHashMap<K, V> map, final boolean asView) {
      this.map = map;
      this.hashes = map.hashes;
      this.keys = map.keys;
      this.values = map.values;
      this.asView = asView;
      this.index = LinearProbingHashMap.findNext(hashes, 0, 0);
   }

   @Override
   public boolean hasNext() {
      return index != -1;
   }

   @Override
   public MutableEntry<K, V> next() {
      if (!hasNext()) {
         throw new NoSuchElementException();
      }
      MutableEntry<K, V> entry = this;
      if (!this.asView) {
         entry = new MutableEntryImpl<>(this.map, this.hashCode(), this.getKey(), this.getValue());
      }

      int index = this.index;
      this.prev = index;
      this.index = LinearProbingHashMap.findNext(hashes, index, 1);
      return entry;
   }

   @Override
   public void remove() {
      if (this.prev == -1) {
         throw new IllegalStateException("Call next() first.");
      }
      this.map.removeAt(this.prev);
      this.prev = -1;
   }

   //-------------------------------------------------------------------------------------------------------------

   @Override
   public K getKey() {
      return (K) this.keys[this.index];
   }

   @Override
   public V getValue() {
      return (V) this.values[this.index];
   }

   @Override
   public void setValue(V value) {
      this.values[this.index] = value;
   }

   //-------------------------------------------------------------------------------------------------------------

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof MutableEntry)) {
         return false;
      }

      MutableEntry<K, ?> that = (MutableEntry<K, ?>) o;
      return this.map.equal(getKey(), that.getKey());
   }

   @Override
   public int hashCode() {
      return this.hashes[this.index];
   }
}
