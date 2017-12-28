package net.andreho.struct.map.impl;

import net.andreho.struct.Reservable;
import net.andreho.struct.iterator.impl.AbstractDelegatingIterator;
import net.andreho.struct.map.ImmutableEntry;
import net.andreho.struct.map.MutableMap;
import net.andreho.struct.set.abstr.AbstractMutableSet;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * Created by a.hofmann on 07.07.2016.
 */
public class MutableMapBackedSet<E>
   extends AbstractMutableSet<E>
   implements Reservable {

   private final MutableMap<E, ?> map;

   public MutableMapBackedSet(final MutableMap<E, ?> map) {
      this.map = requireNonNull(map);
   }

   @Override
   public boolean add(final E e) {
      return map.put(e, null) == map.getDefaultValue();
   }

   @Override
   public boolean remove(final Object o) {
      return map.remove(o) != map.getDefaultValue();
   }

   @Override
   public boolean contains(final Object o) {
      return map.containsKey(o);
   }

   @Override
   public void clear() {
      map.clear();
   }

   @Override
   public boolean reserve(final int capacity) {
      return map.reserve(capacity);
   }

   @Override
   public Iterator<E> iterator() {
      final Iterator<ImmutableEntry<E, ?>> iterator = (Iterator) map.iterator();
      return new AbstractDelegatingIterator<E, ImmutableEntry<E, ?>>(iterator) {
         @Override
         protected E retrieveValue(final ImmutableEntry<E, ?> value) {
            return value.getKey();
         }
      };
   }
}
