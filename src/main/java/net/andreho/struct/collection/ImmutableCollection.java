package net.andreho.struct.collection;

import net.andreho.struct.Measurable;
import net.andreho.struct.Structure;

import java.lang.reflect.Array;

import static java.util.Objects.requireNonNull;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface ImmutableCollection<E> extends Measurable, Structure, Iterable<E> {
   /**
    * @param e
    * @return
    */
   boolean contains(Object e);

   /**
    * @param values
    * @return
    */
   default boolean containsAll(Object... values) {
      requireNonNull(values, "Given array is null.");
      for (Object elem : values) {
         if (!contains(elem)) {
            return false;
         }
      }
      return true;
   }

   /**
    * @param iterable
    * @return
    */
   default boolean containsAll(Iterable<?> iterable) {
      requireNonNull(iterable, "Given iterable is null.");
      for (Object elem : iterable) {
         if (!contains(elem)) {
            return false;
         }
      }
      return true;
   }

   /**
    * @param type of the array to create
    * @param <T>
    * @return an array containing so much as possible elements from this collection
    */
   default <T> T[] toArray(Class<T> type) {
      return toArray((T[]) Array.newInstance(type, size()));
   }

   /**
    * @param array
    * @param <T>
    * @return
    */
   <T> T[] toArray(T[] array);
}
