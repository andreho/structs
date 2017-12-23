package net.andreho.struct.primitive;

import net.andreho.struct.Cleanable;
import net.andreho.struct.Measurable;
import net.andreho.struct.Reservable;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by 666 on 07.08.2016.
 */
public interface IntObjMap<V> extends Measurable, Cleanable, Reservable {

   V get(int key);

   V put(int key, V value);

   V remove(int key);

   boolean containsKey(int key);

   boolean containsValue(Object value);

   EntryView<V> entryView();

   IntSet keySet();

   Collection<V> values();

   //----------------------------------------------------------------------------------------------------------------

   default V getDefaultValue(){
      return null;
   }

   default V putIfAbsent(int key, V value) {
      V v = get(key);
      if (v == getDefaultValue()) {
         v = put(key, value);
      }
      return v;
   }

   default V getOrDefault(int key, V defaultValue) {
      V v;
      return (((v = get(key)) != getDefaultValue()) || containsKey(key))
             ? v
             : defaultValue;
   }

   default boolean remove(int key, Object value) {
      Object current = get(key);
      if (!Objects.equals(current, value) ||
          (current == getDefaultValue() && !containsKey(key))) {
         return false;
      }
      remove(key);
      return true;
   }

   default V replace(int key, V replacement) {
      V curValue;
      if (((curValue = get(key)) != getDefaultValue()) || containsKey(key)) {
         curValue = put(key, replacement);
      }
      return curValue;
   }

   default boolean replace(int key, V oldValue, V newValue) {
      Object curValue = get(key);
      if (!Objects.equals(curValue, oldValue) ||
          (curValue == getDefaultValue() && !containsKey(key))) {
         return false;
      }
      put(key, newValue);
      return true;
   }

   /**
    * @param <V>
    */
   interface EntryView<V> extends PrimitiveEntryView<EntryView<V>> {
      /**
       * @return
       */
      int getKey();

      /**
       * @return
       */
      V getValue();

      /**
       * @param value
       * @return
       */
      V setValue(V value);
   }
}
