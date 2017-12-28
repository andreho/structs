package net.andreho.struct;

import java.util.Objects;

/**
 * <br/>Created by a.hofmann on 03.06.2017 at 04:50.
 */
public abstract class Constants {

  static volatile HashCodeStrategy<Object> DEFAULT_HASHCODE = Objects::hashCode;
  static volatile EqualityStrategy<Object> DEFAULT_EQUALITY_STRATEGY = Objects::equals;

  private Constants() {
  }
}
