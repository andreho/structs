package net.andreho.struct.map;

import net.andreho.struct.Reservable;
import net.andreho.struct.collection.MutableCollection;
import net.andreho.struct.map.impl.linear.LinearProbingHashMap;
import net.andreho.struct.set.MutableSet;

import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface MutableMap<K, V>
  extends ImmutableMap<K, V>,
          Reservable {

  /**
   * @param <K>
   * @param <V>
   * @return
   */
  static <K, V> MutableMap<K, V> createDefault() {
    return new LinearProbingHashMap<>();
  }

  //-----------------------------------------------------------------------------------------------------------------

  /**
   * @param key
   * @param value
   * @return replaced value or {@link #getDefaultValue() default value} it not present
   */
  V put(K key,
        V value);

  /**
   * @param key to remove
   * @return removed value or {@link #getDefaultValue() default value} it not present
   */
  V remove(Object key);

  /**
   * @return a mutable key-set view of this map
   */
  @Override
  MutableSet<K> keySet();

  /**
   * @return entry view of this map
   */
  @Override
  Iterable<MutableEntry<K, V>> entryView();

  /**
   * @return an immutable collection view of this map
   */
  @Override
  MutableCollection<V> values();

  /**
   * @param key
   * @param value
   * @return
   */
  V putIfAbsent(K key,
                V value);

  /**
   * @param key
   * @param value
   * @return
   */
  boolean remove(K key,
                 V value);

  /**
   * @param key
   * @param replacement
   * @return
   */
  V replace(K key,
            V replacement);

  /**
   * @param key
   * @param oldValue
   * @param newValue
   * @return
   */
  boolean replace(K key,
                  V oldValue,
                  V newValue);

  /**
   * @param key
   * @param value
   * @return
   */
  default MutableMap<K, V> add(K key,
                               V value) {
    put(key, value);
    return this;
  }

  /**
   * @param otherMap
   * @return
   */
  default MutableMap<K, V> addAll(Map<? extends K, ? extends V> otherMap) {
    requireNonNull(otherMap);
    putAll(otherMap);
    return this;
  }

  /**
   * @param otherMap
   * @return
   */
  default MutableMap<K, V> addAll(ImmutableMap<? extends K, ? extends V> otherMap) {
    requireNonNull(otherMap);
    putAll(otherMap);
    return this;
  }

  /**
   * @param otherMap
   */
  default void putAll(Map<? extends K, ? extends V> otherMap) {
    requireNonNull(otherMap);
    reserve(otherMap.size());
    for (Map.Entry<? extends K, ? extends V> entry : otherMap.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * @param otherMap
   */
  default void putAll(ImmutableMap<? extends K, ? extends V> otherMap) {
    requireNonNull(otherMap);
    reserve(otherMap.size());
    for (ImmutableEntry<? extends K, ? extends V> entry : otherMap.entryView()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  /**
   *
   */
  void clear();

  //-----------------------------------------------------------------------------------------------------------------

  /**
   * @return a map view of this mutable map
   */
  Map<K, V> toMap();
}
