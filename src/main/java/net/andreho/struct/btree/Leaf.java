package net.andreho.struct.btree;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public class Leaf<K, V> extends Node<K, V> {
   protected Leaf<K, V> prev;
   protected Leaf<K, V> next;

   @Override
   public boolean isLeaf() {
      return true;
   }
}
