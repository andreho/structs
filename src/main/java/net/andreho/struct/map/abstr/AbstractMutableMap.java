package net.andreho.struct.map.abstr;

import net.andreho.struct.map.ImmutableMap;
import net.andreho.struct.map.MutableEntry;
import net.andreho.struct.map.MutableMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <br/>Created by a.hofmann on 24.01.2016.<br/>
 */
public abstract class AbstractMutableMap<K, V> implements MutableMap<K, V>, Map<K, V> {

   protected int size;

   @Override
   public int size() {
      return size;
   }

   @Override
   public boolean isEmpty() {
      return size == 0;
   }

   @Override
   public V getDefaultValue() {
      return null;
   }

   @Override
   public Iterable<MutableEntry<K, V>> entryView() {
      return null;
   }

   @Override
   public V putIfAbsent(final K key, final V value) {
      V v = get(key);
      if (v == null && !containsKey(key)) {
         v = put(key, value);
      }

      return v;
   }

   @Override
   public V getOrDefault(final Object key, final V defaultValue) {
      V v;
      return (((v = get(key)) != null) || containsKey(key))
             ? v
             : defaultValue;
   }

   @Override
   public V replace(final K key, final V replacement) {
      V curValue;
      if (((curValue = get(key)) != null) || containsKey(key)) {
         curValue = put(key, replacement);
      }
      return curValue;
   }

   @Override
   public boolean replace(final K key, final V oldValue, final V newValue) {
      Object curValue = get(key);
      if (!Objects.equals(curValue, oldValue) ||
          (curValue == null && !containsKey(key))) {
         return false;
      }
      put(key, newValue);
      return true;
   }

   @Override
   public boolean remove(final Object key, final Object value) {
      Object curValue = get(key);
      if (!Objects.equals(curValue, value) ||
          (curValue == null && !containsKey(key))) {
         return false;
      }
      remove(key);
      return true;
   }

   @Override
   public void putAll(final Map<? extends K, ? extends V> otherMap) {
      Objects.requireNonNull(otherMap);
      reserve(otherMap.size());
      for (Map.Entry<? extends K, ? extends V> entry : otherMap.entrySet()) {
         put(entry.getKey(), entry.getValue());
      }
   }

   @Override
   public Map<K, V> toMap() {
      return null;
   }

   @Override
   public Set<Entry<K, V>> entrySet() {
      return null;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof ImmutableMap)) {
         if(obj instanceof Map) {
            return equalsToMap((Map)obj);
         }
         return false;
      }
      ImmutableMap m = (ImmutableMap) obj;

      if (size() != m.size()) {
         return false;
      }
      try {
         for (final MutableEntry<K, V> entry : entryView()) {
            final Object key = entry.getKey();
            final V value = entry.getValue();

            if (!m.containsKey(key)) {
               return false;
            }
            if (!Objects.equals(value, m.get(key))) {
               return false;
            }
         }
      } catch (ClassCastException | NullPointerException unused) {
         return false;
      }
      return true;
   }

   private boolean equalsToMap(final Map<K,V> m) {
      if (size() != m.size()) {
         return false;
      }
      try {
         for (final MutableEntry<K, V> entry : entryView()) {
            final K key = entry.getKey();
            final V value = entry.getValue();

            if (value == null) {
               if (!(m.get(key) == null && m.containsKey(key))) {
                  return false;
               }
            } else if (!value.equals(m.get(key))) {
               return false;
            }
         }
      } catch (ClassCastException | NullPointerException unused) {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode() {
      int h = 0;
      Iterator<? extends MutableEntry<K, V>> i = entryView().iterator();
      while (i.hasNext()) {
         h += i.next().hashCode();
      }
      return h;
   }
}
