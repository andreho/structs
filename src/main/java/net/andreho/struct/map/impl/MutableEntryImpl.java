package net.andreho.struct.map.impl;

import net.andreho.struct.map.ImmutableMap;
import net.andreho.struct.map.MutableEntry;

import java.util.Map;
import java.util.Objects;

/**
 * Created by a.hofmann on 12.05.2016.
 */
public class MutableEntryImpl<K, V> implements MutableEntry<K, V> {
   final ImmutableMap<K, V> map;

   private final int hash;
   private final K key;
   private V value;

   public MutableEntryImpl(final ImmutableMap<K, V> map, final int hash, final K key, final V value) {
      this.map = map;
      this.hash = hash;
      this.key = key;
      this.value = value;
   }

   @Override
   public K getKey() {
      return this.key;
   }

   @Override
   public V getValue() {
      return this.value;
   }

   @Override
   public V setValue(V value) {
      V old = this.value;
      this.value = value;
      return old;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof MutableEntry)) {
         if(o instanceof Map.Entry) {
            return equalsToEntry((Map.Entry) o);
         }
         return false;
      }
      MutableEntry<K, ?> other = (MutableEntry<K, ?>) o;
      return
         this.map.equal(getKey(), other.getKey()) &&
         Objects.equals(getValue(), other.getValue());
   }

   private boolean equalsToEntry(final Map.Entry<K,V> entry) {
      return
         this.map.equal(getKey(), entry.getKey()) &&
         Objects.equals(getValue(), entry.getValue());
   }

   @Override
   public int hashCode() {
      return this.hash ^ Objects.hashCode(value);
   }
}
