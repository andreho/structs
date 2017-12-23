package net.andreho.struct.btree;

import java.util.Comparator;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public class BTree<K, V> {
   protected final Comparator<K> comparator;

   //----------------------------------------------------------------------------------------------------------------

   public BTree(Comparator<K> comparator) {
      this.comparator = comparator;
   }

   //----------------------------------------------------------------------------------------------------------------

   public Comparator<K> getComparator() {
      return comparator;
   }
}
