package net.andreho.struct;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface HashCodeStrategy<T> {
   /**
    * Computes a hashcode of the given instance
    * @param value to compute
    * @return a hashcode of the given instance
    */
   int hashCode(T value);

   /**
    * @param <V>
    * @return
    */
   static <V> HashCodeStrategy<V> defaultHashcode() {
      return (HashCodeStrategy<V>) Constants.DEFAULT_HASHCODE;
   }

   /**
    * @param delegate
    * @param <V>
    * @return
    */
   static <V> HashCodeStrategy<V> notNullHashCodeStrategy(final HashCodeStrategy<V> delegate) {
      return (value) ->
         delegate.hashCode(
            requireNonNull(value, "Given value for hashcode can't be null.")
         );
   }
}
