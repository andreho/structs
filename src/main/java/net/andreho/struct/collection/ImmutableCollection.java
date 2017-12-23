package net.andreho.struct.collection;

import net.andreho.struct.Measurable;
import net.andreho.struct.Reservable;
import net.andreho.struct.Structure;

import java.lang.reflect.Array;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface ImmutableCollection<E> extends Measurable, Structure, Reservable, Iterable<E> {
   /**
    * @param e
    * @return
    */
   boolean contains(Object e);

   /**
    * @param values
    * @return
    */
   boolean containsAll(Object... values);

   /**
    * @param iterable
    * @return
    */
   boolean containsAll(Iterable<?> iterable);

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
