package net.andreho.struct.set.abstr;

import net.andreho.struct.collection.impl.AbstractMutableCollection;
import net.andreho.struct.set.MutableSet;

import java.util.Objects;
import java.util.Set;

/**
 * <br/>Created by andreho on 3/30/16 at 8:27 PM.<br/>
 */
public abstract class AbstractMutableSet<E> extends AbstractMutableCollection<E> implements MutableSet<E>, Set<E> {
   /**
    * Current size of this set
    */
   protected int size;

   protected boolean equal(E a, E b) {
      return Objects.equals(a, b);
   }

   protected int hashCode(E value) {
      return Objects.hashCode(value);
   }

   @Override
   public int size() {
      return this.size;
   }
}
