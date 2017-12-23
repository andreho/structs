package net.andreho.struct.set;

import net.andreho.struct.collection.ImmutableCollection;
import net.andreho.struct.collection.MutableCollection;

import java.util.Collection;
import java.util.Set;

/**
 * <br/>Created by a.hofmann on 23.01.2016.<br/>
 */
public interface MutableSet<E> extends ImmutableSet<E>, MutableCollection<E>, Set<E> {
   /**
    * @param c
    * @return
    */
   boolean retainAll(Collection<?> c);

   /**
    * @param c
    * @return
    */
   boolean retainAll(ImmutableCollection<?> c);
}
