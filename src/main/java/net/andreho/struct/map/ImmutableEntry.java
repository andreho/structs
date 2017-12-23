package net.andreho.struct.map;

import net.andreho.struct.Structure;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public interface ImmutableEntry<K, V> extends Structure {
   /**
    * @return
    */
   K getKey();

   /**
    * @return
    */
   V getValue();
}
