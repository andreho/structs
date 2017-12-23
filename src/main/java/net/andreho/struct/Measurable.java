package net.andreho.struct;

/**
 * <br/>Created by a.hofmann on 22.01.2016.<br/>
 */
public interface Measurable {

  /**
   * @return count of associated elements in range [0, 2147483640)
   */
  int size();

  /**
   * @return <b>true</b> if this structure is empty (has no associated elements),
   * <b>false</b> otherwise (has some elements).
   */
  boolean isEmpty();
}
