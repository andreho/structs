package net.andreho.struct.list.impl;

import net.andreho.struct.list.MutableList;

import java.util.ArrayList;
import java.util.Collection;

public class MutableArrayList<E> extends ArrayList<E> implements MutableList<E> {
   public MutableArrayList() {
   }

   public MutableArrayList(final int initialCapacity) {
      super(initialCapacity);
   }

   public MutableArrayList(final Collection<? extends E> c) {
      super(c);
   }
}
