package net.andreho.struct.collection;

import net.andreho.struct.Cleanable;

import java.util.Collection;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface MutableCollection<E> extends ImmutableCollection<E>, Collection<E>, Cleanable {

   /**
    * @param values to add
    * @return <tt>true</tt> if this collection changed as a result of the call
    * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
    *                                       is not supported by this collection
    * @throws ClassCastException            if the class of an element of the specified
    *                                       collection prevents it from being added to this collection
    * @throws NullPointerException          if the specified collection contains a
    *                                       null element and this collection does not permit null elements,
    *                                       or if the specified collection is null
    * @throws IllegalArgumentException      if some property of an element of the
    *                                       specified collection prevents it from being added to this
    *                                       collection
    * @throws IllegalStateException         if not all the elements can be added at
    *                                       this time due to insertion restrictions
    */
   default boolean addAll(E... values) {
      requireNonNull(values, "Given array is null.");
      boolean modified = false;
      for (E value : values) {
         if(add(value)) {
            modified = true;
         }
      }
      return modified;
   }

   /**
    * @param iterable whose elements needs to be added
    * @return <tt>true</tt> if this collection changed as a result of the call
    * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
    *                                       is not supported by this collection
    * @throws ClassCastException            if the class of an element of the specified
    *                                       collection prevents it from being added to this collection
    * @throws NullPointerException          if the specified collection contains a
    *                                       null element and this collection does not permit null elements,
    *                                       or if the specified collection is null
    * @throws IllegalArgumentException      if some property of an element of the
    *                                       specified collection prevents it from being added to this
    *                                       collection
    * @throws IllegalStateException         if not all the elements can be added at
    *                                       this time due to insertion restrictions
    */
   default boolean addAll(Iterable<? extends E> iterable) {
      requireNonNull(iterable, "Given iterable is null.");
      boolean modified = false;
      for (E value : iterable) {
         if(add(value)) {
            modified = true;
         }
      }
      return modified;
   }

   /**
    * @param values
    * @return
    */
   default boolean removeAll(Object... values) {
      requireNonNull(values, "Given array is null.");
      boolean modified = false;
      for (Object value : values) {
         if(remove(value)) {
            modified = true;
         }
      }
      return modified;
   }

   /**
    * @param iterable
    * @return
    */
   default boolean removeAll(Iterable<? extends E> iterable) {
      requireNonNull(iterable, "Given iterable is null.");
      boolean modified = false;
      for (Object value : iterable) {
         if(remove(value)) {
            modified = true;
         }
      }
      return modified;
   }

   /**
    * Removes from this collection all of its elements that are not contained in the specified collection
    *
    * @param collection
    * @return
    */
   default boolean retainAll(ImmutableCollection<?> collection) {
      requireNonNull(collection, "Given collection is null.");
      boolean modified = false;
      Iterator<E> it = iterator();
      while (it.hasNext()) {
         if (!collection.contains(it.next())) {
            it.remove();
            modified = true;
         }
      }
      return modified;
   }
}
