package net.andreho.struct.collection;

import net.andreho.struct.Cleanable;

import java.util.Collection;

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
   boolean addAll(E... values);

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
   boolean addAll(Iterable<? extends E> iterable);

   /**
    * @param values
    * @return
    */
   boolean removeAll(Object... values);

   /**
    * @param iterable
    * @return
    */
   boolean removeAll(Iterable<? extends E> iterable);

   /**
    * Removes from this collection all of its elements that are not contained in the specified collection
    *
    * @param collection
    * @return
    */
   boolean retainAll(ImmutableCollection<?> collection);
}
