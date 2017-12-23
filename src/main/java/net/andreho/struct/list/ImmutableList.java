package net.andreho.struct.list;

import net.andreho.struct.collection.ImmutableCollection;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface ImmutableList<E> extends ImmutableCollection<E> {
   /**
    * @param index of the element to return
    * @return the element at the specified position in this list
    */
   E get(int index);

   /**
    * @param o element to search for
    * @return the index of the first occurrence of the specified element in
    *         this list, or -1 if this list does not contain the element
    */
   int indexOf(Object o);

   /**
    * @param o element to search for
    * @return the index of the last occurrence of the specified element in
    *         this list, or -1 if this list does not contain the element
    */
   int lastIndexOf(Object o);
}
