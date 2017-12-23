package net.andreho.struct.btree;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public class Node<K, V> {
   protected K[] keys;
   protected V[] values;

   public boolean isLeaf() {
      return false;
   }

   public int keyIndex(K key) {
      return -1;
   }

   public int nearestKeyIndex(K key) {
      return -1;
   }
}
