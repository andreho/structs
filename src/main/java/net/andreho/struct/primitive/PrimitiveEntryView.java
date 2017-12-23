package net.andreho.struct.primitive;

/**
 * Created by 666 on 10.08.2016.
 */
public interface PrimitiveEntryView<V extends PrimitiveEntryView<V>> extends Iterable<V> {
   @Override
   int hashCode();
   @Override
   boolean equals(Object other);
}
