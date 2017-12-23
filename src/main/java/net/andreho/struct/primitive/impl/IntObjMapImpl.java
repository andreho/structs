package net.andreho.struct.primitive.impl;

import net.andreho.struct.primitive.IntObjMap;
import net.andreho.struct.primitive.IntSet;
import net.andreho.utils.MathUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by 666 on 07.08.2016.
 */
public class IntObjMapImpl<V>
  extends AbstractPrimitiveToObjMap<V>
  implements IntObjMap<V> {

  /**
   * Default unique private marker object that is used to mark slots used for empty or deleted entries
   */
  private static final Object MARKER = new Object();
  /**
   * Default 'empty' key representative that is used to mark an empty slot
   */
  private static final int DEFAULT_EMPTY_KEY = 0;
  /**
   * Default 'deleted' key representative that is used to mark an removed slot
   */
  private static final int DEFAULT_DELETED_KEY = -1;
  /**
   * Default capacity for this map type
   */
  private static final int DEFAULT_CAPACITY = 8;
  /**
   * Default load factor for this map type
   */
  private static final float DEFAULT_LOAD_FACTOR = 0.8f;
  /**
   * Default maximal count of possible collision before an exception is thrown
   */
  private static final int MAX_COUNT_OF_COLLISIONS = 1024;
  /**
   * Default count of bits to shift for hash preparation/mixing
   */
  private static final int DEFAULT_KEY_SHIFT_FOR_HASH = 11;
  /**
   * Default maximal capacity of this map type
   */
  private static final int DEFAULT_MAX_CAPACITY = 1 << 30;
  /**
   * Default additional space to allocate for special use-case treatment
   */
  private static final int DEFAULT_ADDITIONAL_CAPACITY = 2;
  /**
   * A flag to signal that the used put-routine may lead to a resize
   */
  private static final boolean DO_RESIZE = true;
  /**
   * A flag to signal that the used put-routine may NOT lead to a resize
   */
  private static final boolean DO_NOT_RESIZE = false;

  //----------------------------------------------------------------------------------------------------------------

  private final float loadFactor;
  private int threshold;
  private int mask;
  private int[] keys;
  private Object[] values;

  private IntSet intSet;
  private Collection<V> collection;

  //----------------------------------------------------------------------------------------------------------------

  public IntObjMapImpl() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  //----------------------------------------------------------------------------------------------------------------

  public IntObjMapImpl(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public IntObjMapImpl(int initialCapacity,
                       final float loadFactor) {
    initialCapacity = MathUtils.currentPowerOfTwo(initialCapacity);
    this.mask = initialCapacity - 1;
    this.loadFactor = loadFactor;
    this.keys = allocateKeyStorage(initialCapacity);
    this.values = allocateValueStorage(initialCapacity);
    this.threshold = (int) (initialCapacity * loadFactor);

    this.values[initialCapacity] = MARKER;
    this.values[initialCapacity + 1] = MARKER;
  }

  //----------------------------------------------------------------------------------------------------------------

  private int[] allocateKeyStorage(final int newCapacity) {
    return new int[newCapacity + DEFAULT_ADDITIONAL_CAPACITY];
  }

  private Object[] allocateValueStorage(final int newCapacity) {
    return new Object[newCapacity + DEFAULT_ADDITIONAL_CAPACITY];
  }

  private int hash(final int key) {
    return key ^ (key >>> DEFAULT_KEY_SHIFT_FOR_HASH);
  }

  private int index(final int hash,
                    final int mask) {
    return hash & mask;
  }

  private int collision(final int key,
                        final int index,
                        final int iteration,
                        final int mask) {
    return (index + 1) & mask;
  }

  private V replaceAt(final int index,
                      final Object newValue,
                      final boolean mayResize) {
    if (!mayResize) {
      throw new IllegalStateException("Unreachable.");
    }
    Object currentValue = this.values[index];
    this.values[index] = newValue;
    return unwrapNullValue(currentValue);
  }

  private V unwrapMarkerValue(Object value) {
    return value == MARKER ? getDefaultValue() : (V) value;
  }

  private V unwrapNullValue(Object value) {
    return value == null ? getDefaultValue() : (V) value;
  }

  private V putEmptyKey(final V value) {
    return putMarker(this.keys.length - DEFAULT_ADDITIONAL_CAPACITY, DEFAULT_EMPTY_KEY, value);
  }

  private V putDeletedKey(final V value) {
    return putMarker(this.keys.length - DEFAULT_ADDITIONAL_CAPACITY + 1, DEFAULT_DELETED_KEY, value);
  }

  private V putMarker(final int index,
                      final int key,
                      final Object value) {
    this.keys[index] = key;
    final Object currentValue = this.values[index];
    this.values[index] = value;
    return unwrapMarkerValue(currentValue);
  }

  private int nextCapacity() {
    return nextCapacity(this.keys.length - DEFAULT_ADDITIONAL_CAPACITY);
  }

  private int nextCapacity(final int currentCapacity) {
    return Math.max(DEFAULT_CAPACITY, currentCapacity * 2);
  }

  private Object getDeletedMarkerValue() {
    return this.values[this.values.length - DEFAULT_ADDITIONAL_CAPACITY + 1];
  }

  private Object getEmptyMarkerValue() {
    return this.values[this.values.length - DEFAULT_ADDITIONAL_CAPACITY];
  }

  //----------------------------------------------------------------------------------------------------------------

  @Override
  public V put(final int key,
               final V value) {
    if (key == DEFAULT_EMPTY_KEY) {
      return putEmptyKey(value);
    } else if (key == DEFAULT_DELETED_KEY) {
      return putDeletedKey(value);
    }
    return putInternally(key, value, keys, values, mask, DO_RESIZE);
  }

  private V putInternally(final int key,
                          final Object value,
                          final int[] keys,
                          final Object[] values,
                          final int mask,
                          final boolean mayResize) {
    int index = index(hash(key), mask);

    for (int iteration = 0; iteration < MAX_COUNT_OF_COLLISIONS; iteration++) {
      final int current = keys[index];

      if (key == current) {
        return replaceAt(index, value, mayResize);
      } else if (current == DEFAULT_EMPTY_KEY) {
        return putEntry(index, key, value, keys, values, mayResize);
      }
      index = collision(key, index, iteration, mask);
    }
    throw new IllegalStateException("This map has too many collisions.");
  }

  private V putEntry(final int index,
                     final int key,
                     final Object value,
                     final int[] keys,
                     final Object[] values,
                     final boolean mayResize) {
    keys[index] = key;
    values[index] = value;

    if (mayResize && (++this.size >= this.threshold)) {
      resize(nextCapacity());
    }

    return getDefaultValue();
  }

  private void resize(final int newCapacity) {
    final int newMask = newCapacity - 1;

    final int[] oldKeys = this.keys;
    final Object[] oldValues = this.values;

    final int[] newKeys = allocateKeyStorage(newCapacity);
    final Object[] newValues = allocateValueStorage(newCapacity);

    if (oldKeys.length != oldValues.length) {
      throw new IllegalStateException("Unreachable.");
    }

    for (int i = 0, l = oldKeys.length - DEFAULT_ADDITIONAL_CAPACITY; i < l; i++) {
      final int currentKey = oldKeys[i];
      if (currentKey == DEFAULT_EMPTY_KEY ||
          currentKey == DEFAULT_DELETED_KEY) {
        continue;
      }
      putInternally(currentKey, oldValues[i], newKeys, newValues, newMask, DO_NOT_RESIZE);
    }

    copySpecialMarkers(newCapacity, oldKeys, oldValues, newKeys, newValues);

    mask = newMask;
    keys = newKeys;
    values = newValues;
    threshold = (int) (newCapacity * loadFactor);
    Arrays.fill(oldValues, null);
  }

  private void copySpecialMarkers(final int newCapacity,
                                  final int[] oldKeys,
                                  final Object[] oldValues,
                                  final int[] newKeys,
                                  final Object[] newValues) {
    newKeys[newCapacity] =
      oldKeys[oldKeys.length - DEFAULT_ADDITIONAL_CAPACITY];
    newKeys[newCapacity + 1] =
      oldKeys[oldKeys.length - DEFAULT_ADDITIONAL_CAPACITY + 1];

    newValues[newCapacity] =
      oldValues[oldValues.length - DEFAULT_ADDITIONAL_CAPACITY];
    newValues[newCapacity + 1] =
      oldValues[oldValues.length - DEFAULT_ADDITIONAL_CAPACITY + 1];
  }

  @Override
  public V get(final int key) {
    if (key == DEFAULT_EMPTY_KEY) {
      return unwrapMarkerValue(getEmptyMarkerValue());
    } else if (key == DEFAULT_DELETED_KEY) {
      return unwrapMarkerValue(getDeletedMarkerValue());
    }

    final int[] keys = this.keys;
    final int mask = this.mask;
    int index = index(hash(key), mask);

    for (int iteration = 0; iteration < MAX_COUNT_OF_COLLISIONS; iteration++) {
      final int current = keys[index];
      if (key == current) {
        return (V) this.values[index];
      } else if (current == DEFAULT_EMPTY_KEY) {
        return getDefaultValue();
      }
      index = collision(key, index, iteration, mask);
    }
    throw new IllegalStateException("This map has too many collisions.");
  }

  @Override
  public V remove(final int key) {
    return null;
  }

  @Override
  public boolean containsKey(final int key) {
    if (key == DEFAULT_EMPTY_KEY) {
      return getEmptyMarkerValue() != MARKER;
    } else if (key == DEFAULT_DELETED_KEY) {
      return getDeletedMarkerValue() != MARKER;
    }

    final int[] keys = this.keys;
    final int mask = this.mask;
    int index = index(hash(key), mask);

    for (int iteration = 0; iteration < MAX_COUNT_OF_COLLISIONS; iteration++) {
      final int current = keys[index];
      if (key == current) {
        return true;
      } else if (current == DEFAULT_EMPTY_KEY) {
        return false;
      }
      index = collision(key, index, iteration, mask);
    }
    throw new IllegalStateException("This map has too many collisions.");
  }

  @Override
  public boolean containsValue(final Object value) {
    for (int i = mask + 1; i < this.values.length; i++) {
      Object currentValue = this.values[i];
      if (currentValue != MARKER &&
          Objects.equals(value, currentValue)) {
        return true;
      }
    }
    for (int i = 0, l = mask + 1; i < l; i++) {
      if (Objects.equals(value, this.values[i])) {
        final int key = this.keys[i];
        return key != DEFAULT_EMPTY_KEY &&
               key != DEFAULT_DELETED_KEY;
      }
    }
    return false;
  }

  @Override
  public boolean reserve(final int capacity) {
    if (capacity > DEFAULT_MAX_CAPACITY) {
      return false;
    }
    if (this.keys.length < capacity) {
      int nextCapacity = this.keys.length;
      while (nextCapacity < capacity) {
        nextCapacity = nextCapacity(nextCapacity);
      }
      resize(nextCapacity);
      return true;
    }
    return false;
  }

  @Override
  public void clear() {
    this.size = 0;
    this.mask = DEFAULT_CAPACITY - 1;
    this.keys = allocateKeyStorage(DEFAULT_CAPACITY);
    this.values = allocateValueStorage(DEFAULT_CAPACITY);
    this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
  }

  @Override
  public IntSet keySet() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  @Override
  public EntryView<V> entryView() {
    return null;
  }

  @Override
  public int size() {
    return super.size() +
           (getEmptyMarkerValue() != MARKER ? 0 : 1) +
           (getDeletedMarkerValue() != MARKER ? 0 : 1);
  }

  //----------------------------------------------------------------------------------------------------------------
}
