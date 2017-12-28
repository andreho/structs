package net.andreho.struct;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface EqualityStrategy<T> {
   /**
    * Checks whether two given objects are equal or not
    *
    * @param a to check
    * @param b to check
    * @return <b>true</b> if the given two objects are equal, <b>false</b> otherwise.
    */
   boolean equal(T a, T b);

   /**
    * @param <V>
    * @return
    */
   static <V> EqualityStrategy<V> defaultEquality() {
      return (EqualityStrategy<V>) Constants.DEFAULT_EQUALITY_STRATEGY;
   }

   /**
    * @param delegate
    * @param <V>
    * @return
    */
   static <V> EqualityStrategy<V> notNullEqualityStrategy(final EqualityStrategy<V> delegate) {
      return (a, b) ->
         delegate.equal(
            requireNonNull(a, "Given first value for hashcode can't be null."),
            requireNonNull(b, "Given second value for hashcode can't be null.")
         );
   }
}
