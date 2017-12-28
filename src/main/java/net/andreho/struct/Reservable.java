package net.andreho.struct;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface Reservable {
   /**
    * @param capacity
    * @return <b>true</b> if wanted amount of space was reserved successfully, <b>false</b> otherwise.
    */
   boolean reserve(int capacity);
}
