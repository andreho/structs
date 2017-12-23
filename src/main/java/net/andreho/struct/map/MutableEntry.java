package net.andreho.struct.map;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public interface MutableEntry<K, V> extends ImmutableEntry<K, V> {
   /**
    * @param value
    */
   void setValue(V value);
}
