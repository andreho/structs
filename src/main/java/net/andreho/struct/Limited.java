package net.andreho.struct;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface Limited extends Measurable {
   /**
    * @return
    */
   int capacity();

   /**
    * @return
    */
   default boolean isFull() {
      return capacity() == size();
   }
}
