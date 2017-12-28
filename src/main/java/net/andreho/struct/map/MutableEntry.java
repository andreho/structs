package net.andreho.struct.map;

import java.util.Map;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public interface MutableEntry<K, V> extends ImmutableEntry<K, V>, Map.Entry<K,V> {
   /**
    * @param value
    */
   V setValue(V value);
}
