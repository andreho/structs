package net.andreho.struct.map;

import net.andreho.struct.Measurable;
import net.andreho.struct.Structure;
import net.andreho.struct.collection.ImmutableCollection;
import net.andreho.struct.set.ImmutableSet;

import java.util.Iterator;
import java.util.Objects;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface ImmutableMap<K, V> extends Structure, Measurable, Iterable<ImmutableEntry<K, V>> {
   /**
    * @param key
    * @return
    */
   V get(Object key);

   /**
    * @param key
    * @return
    */
   boolean containsKey(Object key);

   /**
    * @return
    */
   V getDefaultValue();

   /**
    * @param key
    * @return
    */
   V getOrDefault(Object key, V defaultValue);

   /**
    * Important: <b>null</b> key is not allowed.
    *
    * @param key
    * @return
    */
   default int hashCode(K key) {
      return Objects.hashCode(key);
   }

   /**
    * @param a one of two parameters
    * @param b one of two parameters
    * @return
    */
   default boolean equal(K a, K b) {
      return Objects.equals(a, b);
   }

   /**
    * @param value to search for
    * @return <b>true</b> if there is a value in this map that is equal to the given one, <b>false</b> otherwise.
    */
   boolean containsValue(Object value);

   /**
    * @return an immutable key-set view of this map
    */
   ImmutableSet<K> keySet();

   /**
    * @return an immutable collection view of this map
    */
   ImmutableCollection<V> values();

   /**
    * @return an entry view of this map
    */
   Iterable<? extends ImmutableEntry<K, V>> entryView();

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   default Iterator<ImmutableEntry<K, V>> iterator() {
      return (Iterator<ImmutableEntry<K, V>>) entryView().iterator();
   }
}
