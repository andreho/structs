package net.andreho.struct.set.impl;

import net.andreho.struct.collection.ImmutableCollection;
import net.andreho.struct.set.abstr.AbstractMutableSet;
import net.andreho.utils.ArrayUtils;
import net.andreho.utils.MathUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <br/>Created by andreho on 3/30/16 at 8:26 PM.<br/>
 */
public class LinearProbingHashSet<E> extends AbstractMutableSet<E> {
   /**
    * Default hash value shift (to the right) for better hash distribution
    */
   private static final int DEFAULT_HASH_SHIFT = 13;

   //###############################################################################

   /**
    * Default maximal number of collisions during collision resolution
    */
   private static final int DEFAULT_MAX_COLLISIONS = 1024;

   /**
    * Default "empty" marker hash value
    */
   private static final int DEFAULT_EMPTY_HASH = 0;

   /**
    * Default "deleted" marker hash value
    */
   private static final int DEFAULT_DELETED_HASH =
         MathUtils.unique(DEFAULT_EMPTY_HASH);

   /**
    * Default "masked" hash value
    */
   private static final int DEFAULT_MASKED_HASH =
         MathUtils.unique(DEFAULT_EMPTY_HASH, DEFAULT_DELETED_HASH);

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
   private static final int[] EMPTY_HASH_ARRAY = ArrayUtils.Constants.EMPTY_INT_ARRAY;

   /**
    * Singleton instance for empty object array
    */
   private static final Object[] EMPTY_OBJECT_ARRAY = ArrayUtils.Constants.EMPTY_OBJECT_ARRAY;

   //-----------------------------------------------------------------------------------------------------------------
   /**
    * Current load factor
    */
   private final float loadFactor;
   /**
    * Current bit-mask for hashing
    */
   private int mask;

   //----------------------------------------------------------------------------------------------------------------
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
   private int[] hashes;
   /**
    * Array with elements
    */
   private Object[] elements;

   public LinearProbingHashSet() {
      this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, false);
   }

   public LinearProbingHashSet(int capacity) {
      this(checkCapacity(capacity), DEFAULT_LOAD_FACTOR, false);
   }

   //----------------------------------------------------------------------------------------------------------------

   public LinearProbingHashSet(float loadFactor) {
      this(DEFAULT_CAPACITY, checkLoadFactor(loadFactor), false);
   }

   //----------------------------------------------------------------------------------------------------------------

   public LinearProbingHashSet(int capacity, float loadFactor) {
      this(checkCapacity(capacity), checkLoadFactor(loadFactor), false);
   }

   public LinearProbingHashSet(Collection<? extends E> other) {
      this(checkCapacity(other.size()), DEFAULT_LOAD_FACTOR, false);
      addAll(other);
   }

   public LinearProbingHashSet(ImmutableCollection<? extends E> other) {
      this(checkCapacity(other.size()), DEFAULT_LOAD_FACTOR, false);
      addAll(other);
   }

   private LinearProbingHashSet(int capacity, float loadFactor, boolean dummy) {
      this.loadFactor = loadFactor;

      if (capacity == 0) {
         this.hashes = EMPTY_HASH_ARRAY;
         this.elements = EMPTY_OBJECT_ARRAY;
      } else {
         this.mask = capacity = MathUtils.bitMask(capacity);

         capacity++;

         this.threshold = (int) (loadFactor * capacity);

         this.hashes = new int[capacity];
         this.elements = new Object[capacity];

      }
      initialize(hashes, DEFAULT_EMPTY_HASH);
   }

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

   protected void initialize(final int[] hashes, final int defaultHash) {
      if (defaultHash != 0) {
         Arrays.fill(hashes, defaultHash);
      }
   }

   //----------------------------------------------------------------------------------------------------------------

   @Override
   public boolean equal(E a, E b) {
      return a.equals(b);
   }

   @Override
   public int hashCode(E value) {
      return value.hashCode();
   }

   //----------------------------------------------------------------------------------------------------------------

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
    * @param current   index in the table
    * @param mask      of the table
    * @return new index in the table
    */
   private int collision(int hash, int iteration, int current, int mask) {
      if (iteration >= DEFAULT_MAX_COLLISIONS) {
         throw new IllegalStateException("Max collisions count reached.");
      }

      return (current + iteration + 1) & mask;
   }

   private void resize(int newCapacity) {
      checkCapacity(newCapacity);

      final Object[] curKeys = this.elements;
      final int[] curHashes = this.hashes;

      final Object[] newKeys = new Object[newCapacity];
      final int[] newHashes = new int[newCapacity];

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

               continue loop;
            }

            index = collision(hash, j, index, newMask);
         }
      }

      this.deleted = 0;

      this.mask = newMask;
      this.threshold = (int) (newCapacity * this.loadFactor);

      this.elements = newKeys;
      this.hashes = newHashes;
   }

   //######################################################################

   private void rehash() {
      final int mask = this.mask;
      final Object[] keys = this.elements;
      final int[] hashes = this.hashes;

      //Prepare table, removing all "deleted"-entries.
      for (int i = 0; i < keys.length; i++) {
         if (hashes[i] == DEFAULT_DELETED_HASH) {
            hashes[i] = DEFAULT_EMPTY_HASH;
         }
      }

      outer:
      //Reinsert all available values
      for (int i = 0; i < keys.length; i++) {
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

         hashes[i] = DEFAULT_EMPTY_HASH;
         keys[i] = null;

         for (int j = 0; ; j++) {
            if (hashes[index] == DEFAULT_EMPTY_HASH) {
               hashes[index] = hash;
               keys[index] = key;

               continue outer;
            }

            index = collision(hash, j, index, mask);
         }
      }

      final int size = this.size;
      int reachable = 0;

      //Validate the reachability of every key-value-pair !
      outer:
      while (reachable < size) {
         reachable = 0;

         check:
         for (int i = 0; i < hashes.length; i++) {
            final int hash = hashes[i];
            final E key = (E) keys[i];

            if (hash == DEFAULT_EMPTY_HASH) {
               continue;
            }

            int index = index(hash, mask);

            for (int j = 0; ; j++) {
               int currentHash = hashes[index];
               final Object currentKey = keys[index];

               if (hash == currentHash &&
                   equal(key, (E) currentKey)) {
                  reachable++;
                  continue check;
               } else if (currentHash == DEFAULT_EMPTY_HASH) {
                  hashes[i] = DEFAULT_EMPTY_HASH;
                  keys[i] = null;

                  hashes[index] = hash;
                  keys[index] = key;

                  continue outer;
               }

               index = collision(hash, j, index, mask);
            }
         }
      }

      this.deleted = 0;
   }

   //----------------------------------------------------------------------------------------------------------------

   @SuppressWarnings("Duplicates")
   @Override
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

   //----------------------------------------------------------------------------------------------------------------

   @Override
   public boolean contains(Object key) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode((E) key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == hash) {
            if (equal((E) key, (E) this.elements[index])) {
               return true;
            }
         } else if (currentHash == DEFAULT_EMPTY_HASH) {
            return false;
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   @Override
   public boolean add(E key) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode(key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == DEFAULT_EMPTY_HASH) {
            return addInternal(index, hash, key);
         } else if (currentHash == hash && equal(key, (E) this.elements[index])) {
            return false;
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   private boolean addInternal(int index, int hash, Object key) {
      this.hashes[index] = hash;
      this.elements[index] = key;

      return handleAdd();
   }

   private boolean handleAdd() {
      if ((this.size++ + this.deleted) >= this.threshold) {
         if (this.size < this.threshold) {
            rehash();
         } else {
            resize(nextCapacity());
         }
      }

      return true;
   }

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   public boolean remove(Object key) {
      final int mask = this.mask;
      final int hash = maskHash(hashCode((E) key));
      final int[] hashes = this.hashes;

      for (int index = index(hash, mask), iteration = 0; ; iteration++) {
         int currentHash = hashes[index];

         if (currentHash == hash) {
            if (equal((E) key, (E) this.elements[index])) {
               return removeAt(index);
            }
         } else if (currentHash == DEFAULT_EMPTY_HASH) {
            return false;
         }

         index = collision(hash, iteration, index, mask);
      }
   }

   boolean removeAt(int index) {
      this.hashes[index] = DEFAULT_DELETED_HASH;
      this.elements[index] = null;

      this.size--;
      this.deleted++;

      return true;
   }

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   public Iterator<E> iterator() {
      if (isEmpty()) {
         return Collections.emptyIterator();
      }
      return new LinearProbingHashSetIterator<>(this, this.hashes, this.elements);
   }

   //-----------------------------------------------------------------------------------------------------------------

   @Override
   public void clear() {
      this.size = this.deleted = 0;
      Arrays.fill(this.hashes, DEFAULT_EMPTY_HASH);
      Arrays.fill(this.elements, null);
   }

   //-----------------------------------------------------------------------------------------------------------------

   static final class LinearProbingHashSetIterator<E> implements Iterator<E> {
      //-------------------------------------------------------------------------------------------------------------

      final LinearProbingHashSet<E> owner;

      //-------------------------------------------------------------------------------------------------------------
      final Object[] elements;
      final int[] hashes;
      int prev = -1;
      int index = -1;

      public LinearProbingHashSetIterator(LinearProbingHashSet<E> owner, int[] hashes, Object[] elements) {
         this.owner = owner;
         this.hashes = hashes;
         this.elements = elements;
         findNext();
      }

      //-------------------------------------------------------------------------------------------------------------

      private void findNext() {
         final int[] hashes = this.hashes;
         for (int i = index + 1; i < hashes.length; i++) {
            final int hash = hashes[i];
            if (hash != DEFAULT_EMPTY_HASH && hash != DEFAULT_DELETED_HASH) {
               this.prev = this.index;
               this.index = i;
            }
         }
         this.index = hashes.length;
      }

      @Override
      public boolean hasNext() {
         final int index = this.index;
         return index > -1 && index < this.hashes.length;
      }

      @Override
      public E next() {
         if (!hasNext()) {
            throw new NoSuchElementException("Element isn't available.");
         }
         final Object element = this.elements[this.index];
         findNext();
         return (E) element;
      }

      @Override
      public void remove() {
         if (this.prev < 0) {
            throw new IllegalStateException("Call the next() method first.");
         }
         this.owner.removeAt(this.prev);
         this.prev = -1;
      }
   }

   //-----------------------------------------------------------------------------------------------------------------
}
