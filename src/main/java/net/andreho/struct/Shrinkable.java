package net.andreho.struct;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface Shrinkable extends Measurable {
   /**
    * @return count of unused capacity slots that were freed during the shrinking
    */
   int shrink();
}
