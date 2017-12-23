package net.andreho.struct.map.impl;

import net.andreho.struct.map.ImmutableMap;
import net.andreho.struct.map.MutableEntry;

/**
 * Created by a.hofmann on 12.05.2016.
 */
public class MutableEntryImpl<K, V> implements MutableEntry<K, V> {
   final ImmutableMap<K, V> map;

   //-----------------------------------------------------------------------------------------------------------------
   private final int hash;
   private final K key;
   private V value;

   public MutableEntryImpl(final ImmutableMap<K, V> map, final int hash, final K key, final V value) {
      this.map = map;
      this.hash = hash;
      this.key = key;
      this.value = value;
   }

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   public K getKey() {
      return this.key;
   }

   @Override
   public V getValue() {
      return this.value;
   }

   @Override
   public void setValue(V value) {
      this.value = value;
   }

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof MutableEntry)) {
         return false;
      }
      MutableEntry<K, ?> other = (MutableEntry<K, ?>) o;
      return this.map.equal(getKey(), other.getKey());
   }

   @Override
   public int hashCode() {
      return this.hash;
   }

   //-----------------------------------------------------------------------------------------------------------------
}
