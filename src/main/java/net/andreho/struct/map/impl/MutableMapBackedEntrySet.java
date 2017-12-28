package net.andreho.struct.map.impl;

import net.andreho.struct.Reservable;
import net.andreho.struct.map.MutableEntry;
import net.andreho.struct.map.MutableMap;
import net.andreho.struct.set.abstr.AbstractMutableSet;

import java.util.Iterator;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Created by a.hofmann on 07.07.2016.
 */
public class MutableMapBackedEntrySet<K,V>
   extends AbstractMutableSet<MutableEntry<K,V>>
   implements Reservable {

   private final MutableMap<K, V> map;

   public MutableMapBackedEntrySet(final MutableMap<K, V> map) {
      this.map = requireNonNull(map);
   }

   @Override
   public boolean add(final MutableEntry<K,V> entry) {
      requireNonNull(entry);
      return map.put(entry.getKey(), entry.getValue()) == map.getDefaultValue();
   }

   @Override
   public boolean remove(final Object o) {
      if(!(o instanceof Map.Entry)) {
         return false;
      }
      final Map.Entry<K,V> entry = (Map.Entry<K, V>) o;
      return map.remove(entry.getKey()) != map.getDefaultValue();
   }

   @Override
   public boolean contains(final Object o) {
      if(!(o instanceof Map.Entry)) {
         return false;
      }
      final Map.Entry<K,V> entry = (Map.Entry<K, V>) o;
      return map.containsKey(entry.getKey());
   }

   @Override
   public void clear() {
      map.clear();
   }

   @Override
   public boolean reserve(final int capacity) {
      return map.reserve(capacity);
   }

   @Override
   public Iterator<MutableEntry<K,V>> iterator() {
      return map.entryView().iterator();
   }
}
