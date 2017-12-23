package net.andreho.struct.collection.impl;

import net.andreho.struct.collection.ImmutableCollection;
import net.andreho.struct.collection.MutableCollection;

import java.util.Iterator;
import java.util.Objects;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public abstract class AbstractMutableCollection<E>
      extends java.util.AbstractCollection<E>
      implements MutableCollection<E> {

   @Override
   public boolean addAll(final E... values) {
      Objects.requireNonNull(values);
      boolean modified = false;

      for (E e : values) {
         if (add(e)) {
            modified = true;
         }
      }
      return modified;
   }

   @Override
   public boolean addAll(final Iterable<? extends E> iterable) {
      Objects.requireNonNull(iterable);
      boolean modified = false;

      for (E e : iterable) {
         if (add(e)) {
            modified = true;
         }
      }
      return modified;
   }

   @Override
   public boolean containsAll(Object... values) {
      Objects.requireNonNull(values);
      for (int i = 0, len = values.length; i < len; i++) {
         if (!contains(values[i])) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean containsAll(Iterable<?> iterable) {
      Objects.requireNonNull(iterable);
      for (Object elem : iterable) {
         if (!contains(elem)) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean retainAll(ImmutableCollection<?> coll) {
      Objects.requireNonNull(coll);
      boolean modified = false;

      Iterator<E> it = iterator();
      while (it.hasNext()) {
         if (!coll.contains(it.next())) {
            it.remove();
            modified = true;
         }
      }
      return modified;
   }

   @Override
   public boolean removeAll(Iterable<? extends E> iterable) {
      Objects.requireNonNull(iterable);
      boolean modified = false;

      Iterator<?> it = iterable.iterator();
      while (it.hasNext()) {
         if (remove(it.next())) {
            modified = true;
         }
      }
      return modified;
   }

   @Override
   public boolean removeAll(final Object... values) {
      Objects.requireNonNull(values);
      boolean modified = false;

      for (Object o : values) {
         if (remove(o)) {
            modified = true;
         }
      }
      return modified;
   }
}
