package net.andreho.struct.map.abstr;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <br/>Created by a.hofmann on 18.05.2014 at 05:55<br/>
 */
public abstract class AbstractJavaMap<K, V> implements Map<K, V> {
   //################################################################################################

   @Override
   public boolean isEmpty() {
      return size() == 0;
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> m) {
      for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
         K key = entry.getKey();
         V value = entry.getValue();

         put(key, value);
      }
   }

   @Override
   public Set<K> keySet() {
      return null;
   }

   @Override
   public Collection<V> values() {
      return null;
   }

   @Override
   public Set<Entry<K, V>> entrySet() {
      return null;
   }
}
