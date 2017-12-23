package net.andreho.struct.primitive;

import net.andreho.struct.Cleanable;
import net.andreho.struct.Measurable;

/**
 * Created by 666 on 10.08.2016.
 */
public interface IntCollection extends Cleanable, Measurable {
   /**
    * @param element
    * @return
    */
   boolean add(int element);

   /**
    * @param element
    * @return
    */
   boolean contains(int element);

   /**
    * @param element
    * @return
    */
   boolean remove(int element);

   /**
    * @return
    */
   int[] toArray();

   /**
    * @param array
    * @return
    */
   int[] toArray(int[] array);

   /**
    * @param c
    * @return
    */
   boolean addAll(IntCollection c);

   /**
    * @param c
    * @return
    */
   boolean containsAll(IntCollection c);

   /**
    * @param c
    * @return
    */
   boolean removeAll(IntCollection c);

   /**
    * @param c
    * @return
    */
   boolean retainAll(IntCollection c);
}
