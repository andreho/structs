package net.andreho.struct.map.impl.linear;

import net.andreho.struct.map.MutableEntry;
import net.andreho.struct.map.impl.MutableEntryImpl;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.andreho.struct.map.impl.linear.LinearProbingHashMap.findNext;

/**
 * <br/>Created by a.hofmann on 09.04.2017 at 23:56.
 */
final class LinearProbingMapEntryViewIterator<K, V> implements Iterator<MutableEntry<K, V>>, MutableEntry<K, V> {
   final LinearProbingHashMap<K, V> map;

   private final boolean asView;
   private int prev;
   private int index;

   LinearProbingMapEntryViewIterator(final LinearProbingHashMap<K, V> map) {
      this(map, false);
   }

   LinearProbingMapEntryViewIterator(final LinearProbingHashMap<K, V> map, final boolean asView) {
      requireNonNull(map, "Given delegate-map can't be null.");
      this.map = map;
      this.asView = asView;
      this.index = findNext(map.hashes, 0, 0);
      this.prev = -1;
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

      int index = this.index;
      this.prev = index;
      this.index = findNext(this.map.hashes, index, 1);

      if (this.asView) {
         return this;
      }

      return new MutableEntryImpl<K,V>(this.map, getKeyHashCode(), getKey(), getValue()) {
         @Override
         public V setValue(final V value) {
            LinearProbingMapEntryViewIterator.this.setValue(value);
            return super.setValue(value);
         }
      };
   }

   private int getKeyHashCode() {
      return this.map.hashes[this.prev];
   }

   @Override
   public void remove() {
      if (this.prev == -1) {
         throw new IllegalStateException("Call next() first.");
      }
      this.map.removeAt(this.prev);
      this.prev = -1;
   }

   @Override
   public K getKey() {
      return (K) this.map.keys[this.prev];
   }

   @Override
   public V getValue() {
      return (V) this.map.values[this.prev];
   }

   @Override
   public V setValue(V value) {
      final Object old = this.map.values[this.prev];
      this.map.values[this.prev] = value;
      return (V) old;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Map.Entry)) {
         return false;
      }
      Map.Entry<K, ?> that = (Map.Entry<K, ?>) o;
      return this.map.equal(getKey(), that.getKey()) &&
         Objects.equals(getValue(), that.getValue());
   }

   @Override
   public int hashCode() {
      return getKeyHashCode() ^ Objects.hashCode(getValue());
   }
}
