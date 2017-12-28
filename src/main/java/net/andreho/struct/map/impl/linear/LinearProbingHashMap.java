package net.andreho.struct.map.impl.linear;

import net.andreho.struct.collection.MutableCollection;
import net.andreho.struct.map.ImmutableMap;
import net.andreho.struct.map.MutableEntry;
import net.andreho.struct.map.abstr.AbstractMutableMap;
import net.andreho.struct.set.MutableSet;
import net.andreho.struct.map.impl.MutableMapBackedEntrySet;
import net.andreho.struct.map.impl.MutableMapBackedSet;
import net.andreho.utils.ArrayUtils.Constants;
import net.andreho.utils.MathUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <br/>Created by a.hofmann on 29.01.2016.<br/>
 */
public class LinearProbingHashMap<K, V> extends AbstractMutableMap<K, V> {
   /**
    * Default "empty" marker hash value
    */
   private static final int DEFAULT_EMPTY_HASH = ThreadLocalRandom.current().nextInt();
   /**
    * Default "deleted" marker hash value
    */
   private static final int DEFAULT_DELETED_HASH = MathUtils.unique(DEFAULT_EMPTY_HASH);
   /**
    * Default hash value shift (to the right) for better hash distribution
    */
   private static final int DEFAULT_HASH_SHIFT = 15;
   /**
    * Default "empty" marker hash value
    */
   private static final int DEFAULT_MAX_COLLISIONS = 1024;
   /**
    * Default "masked" hash value
    */
   private static final int DEFAULT_MASKED_HASH = MathUtils.unique(DEFAULT_EMPTY_HASH, DEFAULT_DELETED_HASH);
   /**
    * Minimal capacity
    */
   private static final int MIN_CAPACITY = 0;
   /**
    * Maximal reachable capacity (1073741824)
    */
   private static final int MAX_CAPACITY = 1 << 30;
   /**
    * Default capacity
    */
   private static final int DEFAULT_CAPACITY = 8;
   /**
    * Default load factor
    */
   private static final float DEFAULT_LOAD_FACTOR = 0.75f;
   /**
    * Default minimal load factor
    */
   private static final float MIN_LOAD_FACTOR = 0.10f;
   /**
    * Default maximal load factor
    */
   private static final float MAX_LOAD_FACTOR = 0.95f;
   /**
    * Singleton instance for empty hash array
    */
   private static final int[] EMPTY_HASH_ARRAY = Constants.EMPTY_INT_ARRAY;
   /**
    * Singleton instance for empty object array
    */
   private static final Object[] EMPTY_OBJECT_ARRAY = Constants.EMPTY_OBJECT_ARRAY;

   //----------------------------------------------------------------------------------------------------------------

   /**
    * Current load factor
    */
   private final float loadFactor;
   /**
    * Current bit-mask for hashing
    */
   private int mask;
   /**
    * Current maximal threshold where we need to resize
    */
   private int threshold;
   /**
    * Current count of deleted entries
    */
   private int deleted;
   /**
    * Array with hashes
    */
   int[] hashes;
   /**
    * Array with keys
    */
   Object[] keys;
   /**
    * Array with values
    */
   Object[] values;
   /**
    * Storage for a key-set view
    */
   private volatile MutableSet<K> keySet;
   /**
    * Storage for a collection view of values
    */
   private volatile MutableCollection<V> valueCollection;

   //----------------------------------------------------------------------------------------------------------------

   public LinearProbingHashMap() {
      this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, false);
   }

   public LinearProbingHashMap(int capacity) {
      this(checkCapacity(capacity), DEFAULT_LOAD_FACTOR, false);
   }

   public LinearProbingHashMap(float loadFactor) {
      this(DEFAULT_CAPACITY, checkLoadFactor(loadFactor), false);
   }

   public LinearProbingHashMap(int capacity, float loadFactor) {
      this(checkCapacity(capacity), checkLoadFactor(loadFactor), false);
   }

   public LinearProbingHashMap(Map<? extends K, ? extends V> other) {
      this(checkCapacity(other.size()), DEFAULT_LOAD_FACTOR, false);
      putAll(other);
   }

   public LinearProbingHashMap(ImmutableMap<? extends K, ? extends V> other) {
      this(checkCapacity(other.size()), DEFAULT_LOAD_FACTOR, false);
      putAll(other);
   }

   private LinearProbingHashMap(int capacity, float loadFactor, boolean dummy) {
      this.loadFactor = loadFactor;

      if (capacity == 0) {
         this.hashes = EMPTY_HASH_ARRAY;
         this.keys = EMPTY_OBJECT_ARRAY;
         this.values = EMPTY_OBJECT_ARRAY;
      } else {
         this.mask = capacity = MathUtils.bitMask(capacity);

         capacity++;

         this.threshold = (int) (loadFactor * capacity);

         this.hashes = new int[capacity];
         this.keys = new Object[capacity];
         this.values = new Object[capacity];
      }
      initialize(hashes, DEFAULT_EMPTY_HASH);
   }

   //-----------------------------------------------------------------------------------------------------------------

   /**
    * Load factor must be between {@link #MIN_LOAD_FACTOR} and {@link #MAX_LOAD_FACTOR}
    *
    * @param loadFactor
    * @return suitable load factor
    */
   private static float checkLoadFactor(float loadFactor) {
      if (loadFactor < MIN_LOAD_FACTOR || loadFactor > MAX_LOAD_FACTOR) {
         throw new IllegalArgumentException("Given load-factor is not allowed: " + loadFactor);
      }
      return loadFactor;
   }

   //-----------------------------------------------------------------------------------------------------------------

   /**
    * Capacity may be between 0 and 1073741824 inclusive
    *
    * @param capacity to check
    * @return
    */
   private static int checkCapacity(int capacity) {
      if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
         throw new IllegalArgumentException("Given capacity is not allowed: " + capacity);
      }
      return capacity;
   }

   static int findNext(final int[] hashes, final int index, final int offset) {
      for (int i = index + offset; i < hashes.length; i++) {
         final int h = hashes[i];
         if (h != DEFAULT_EMPTY_HASH &&
             h != DEFAULT_DELETED_HASH) {
            return i;
         }
      }
      return -1;
   }

   protected void initialize(final int[] hashes,
                             final int defaultEmptyHash) {
      if (defaultEmptyHash != 0) {
         Arrays.fill(hashes, defaultEmptyHash);
      }
   }

   @Override
   public int hashCode(K key) {
      return key.hashCode();
   }

   @Override
   public boolean equal(K firstKey, K secondKey) {
      return firstKey.equals(secondKey);
   }

   private int nextCapacity(int cap) {
      return cap << 1;
   }

   /**
    * @return next capacity for this map on a resize
    */
   private int nextCapacity() {
      return nextCapacity(this.hashes.length);
   }

   /**
    * @return previous capacity for this map on a resize
    */
   private int previousCapacity() {
      return this.hashes.length >> 1;
   }

   /**
    * Masks if needed a unsupported hash value with an replacement
    *
    * @param hash to check and mask if needed
    * @return the given hash or a masked one
    */
   private int maskHash(int hash) {
      if (hash == DEFAULT_EMPTY_HASH ||
          hash == DEFAULT_DELETED_HASH) {
         return DEFAULT_MASKED_HASH;
      }
      return hash;
   }

   /**
    * Calculates initial index for given hash value and mask
    *
    * @param hash of a key object
    * @param mask for this map
    * @return initial insertion index
    */
   private int index(int hash, int mask) {
      return ((hash >>> DEFAULT_HASH_SHIFT) ^ hash) & mask;
   }

   /**
    * Calculates next index in case of a collision
    *
    * @param hash      computed hash value for given key
    * @param iteration current number of collision iterations
    * @param index   index in the table
    * @param mask      of the table
    * @return new index in the table
    */
   private int collision(int hash, int iteration, int index, int mask) {
      if (iteration >= DEFAULT_MAX_COLLISIONS) {
         throw new IllegalStateException("Max collisions count reached.");
      }

      return (index + iteration + 1) & mask;
   }

   //-----------------------------------------------------------------------------------------------------------------

   private void resize(int newCapacity) {
      checkCapacity(newCapacity);

      final Object[] curKeys = this.keys;
      final Object[] curValues = this.values;
      final int[] curHashes = this.hashes;

      final Object[] newKeys = new Object[newCapacity];
      final Object[] newValues = new Object[newCapacity];
      final int[] newHashes = new int[newCapacity];
      Arrays.fill(newHashes, DEFAULT_EMPTY_HASH);

      final int newMask = newCapacity - 1;

      loop:
      for (int i = 0; i < curKeys.length; i++) {
         final int hash = curHashes[i];

         if (((hash == DEFAULT_EMPTY_HASH) ||
              (hash == DEFAULT_DELETED_HASH))) {
            continue;
         }

         int index = index(hash, newMask);

         for (int j = 0; ; j++) {
            int curHash = newHashes[index];

            if (curHash == DEFAULT_EMPTY_HASH) {
               newHashes[index] = hash;
               newKeys[index] = curKeys[i];
               newValues[index] = curValues[i];

               continue loop;
            }

            index = collision(hash, j, index, newMask);
         }
      }

      this.deleted = 0;

      this.mask = newMask;
      this.threshold = (int) (newCapacity * this.loadFactor);

      this.keys = newKeys;
      this.values = newValues;
      this.hashes = newHashes;
   }

   /**
    * Rehash process includes following steps:
    * <ul>
    * <li>removal of all tombstones</li>
    * <li>iteratively reinsertion of all key-value pairs</li>
    * <li>validation that all reinserted pairs are reachable</li>
    * <li>reset of the deleted counter</li>
    * </ul>
    */
   private void rehash() {
      final int mask = this.mask;
      final Object[] keys = this.keys;
      final Object[] values = this.values;
      final int[] hashes = this.hashes;

      //Prepare table, removing all "deleted"-entries.
      for (int i = 0; i < hashes.length; i++) {
         if (hashes[i] == DEFAULT_DELETED_HASH) {
            hashes[i] = DEFAULT_EMPTY_HASH;
         }
      }

      //Reinsert all existing key-value pairs
      outer:
      for (int i = 0; i < hashes.length; i++) {
         final int hash = hashes[i];

         if (hash == DEFAULT_EMPTY_HASH) {
            continue;
         }

         int index = index(hash, mask);

         //origin slot => do nothing !
         if (i == index) {
            continue;
         }

         final Object key = keys[i];
         final Object value = values[i];

         hashes[i] = DEFAULT_EMPTY_HASH;

         keys[i] = null;
         values[i] = null;

         for (int j = 0; ; j++) {
            if (hashes[index] == DEFAULT_EMPTY_HASH) {
               hashes[index] = hash;
               keys[index] = key;
               values[index] = value;

               continue outer;
            }

            index = collision(hash, j, index, mask);
         }
      }

      final int size = this.size;
      int reachable = 0;

      //Validate the readability of every key-value-pair!
      outer:
      while (reachable < size) {
         reachable = 0;

         check:
         for (int i = 0; i < hashes.length; i++) {
            final int hash = hashes[i];
            final K key = (K) keys[i];

            if (hash == DEFAULT_EMPTY_HASH) {
               continue;
            }

            final Object value = values[i];
            int index = index(hash, mask);

            for (int j = 0; ; j++) {
               int currentHash = hashes[index];
               final Object currentKey = keys[index];

               if (hash == currentHash &&
                   equal(key, (K) currentKey)) {
                  reachable++;
                  continue check;
               } else if (currentHash == DEFAULT_EMPTY_HASH) {
                  hashes[i] = DEFAULT_EMPTY_HASH;
                  keys[i] = null;
                  values[i] = null;

                  hashes[index] = hash;
                  keys[index] = key;
                  values[index] = value;

                  continue outer;
               }

               index = collision(hash, j, index, mask);
            }
         }
      }
      //Reset deleted counter
      this.deleted = 0;
   }

   @Override
   @SuppressWarnings("Duplicates")
   public boolean reserve(int capacity) {
      capacity = checkCapacity(capacity);

      if (capacity > this.hashes.length) {
         int next = nextCapacity();
         while (next < capacity) {
            next = nextCapacity(next);
         }
         resize(next);
         return true;
      }
      return false;
   }

   @Override
   public V get(Object key) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode((K) key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == hash) {
            if (equal((K) key, (K) this.keys[index])) {
               return (V) this.values[index];
            }
         } else if (currentHash == DEFAULT_EMPTY_HASH) {
            return getDefaultValue();
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   @Override
   public boolean containsKey(final Object key) {
      return get(key) != getDefaultValue();
   }

   @Override
   public V put(K key, V value) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode(key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == DEFAULT_EMPTY_HASH) {
            return putAt(index, hash, key, value);
         } else if (currentHash == hash && equal(key, (K) this.keys[index])) {
            return replaceAt(index, value);
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   @Override
   public V remove(Object key) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode((K) key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == hash) {
            if (equal((K) key, (K) this.keys[index])) {
               return removeAt(index);
            }
         } else if (currentHash == DEFAULT_EMPTY_HASH) {
            return getDefaultValue();
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   @Override
   public boolean containsValue(Object value) {
      final Object[] values = this.values;
      for (int i = 0, len = values.length; i < len; i++) {
         if (Objects.equals(value, values[i])) {
            return true;
         }
      }
      return false;
   }

   @Override
   public void clear() {
      this.deleted = this.size = 0;
      Arrays.fill(this.hashes, DEFAULT_EMPTY_HASH);
      Arrays.fill(this.keys, null);
      Arrays.fill(this.values, getDefaultValue());
   }

   //-----------------------------------------------------------------------------------------------------------------

   V putAt(int index, int hash, Object key, Object value) {
      this.hashes[index] = hash;
      this.keys[index] = key;
      this.values[index] = value;

      if (this.size++ >= this.threshold) {
         resize(nextCapacity());
      }

      return getDefaultValue();
   }

   V removeAt(int index) {
      final V old = (V) this.values[index];
      this.hashes[index] = DEFAULT_DELETED_HASH;
      this.keys[index] = null;
      this.values[index] = null;

      if ((--this.size) + (++this.deleted) >= this.threshold) {
         rehash();
      }

      return old;
   }

   V replaceAt(int index, V value) {
      V current = (V) this.values[index];
      this.values[index] = value;
      return current;
   }

   @Override
   public Map<K, V> toMap() {
      return this;
   }

   @Override
   public MutableSet<K> keySet() {
      if (this.keySet == null) {
         this.keySet = new MutableMapBackedSet<>(this);
      }
      return this.keySet;
   }

   @Override
   public MutableCollection<V> values() {
      if (this.valueCollection == null) {
         this.valueCollection = new LinearProbingMapValueCollection<>(this);
      }
      return this.valueCollection;
   }

   @Override
   public Set<Entry<K, V>> entrySet() {
      return (Set) new MutableMapBackedEntrySet<>(this);
   }

   @Override
   public Iterable<MutableEntry<K, V>> entryView() {
      return new LinearProbingMapEntryViewIterable<>(this, true);
   }
}
