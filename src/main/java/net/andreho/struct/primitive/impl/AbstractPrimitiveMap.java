package net.andreho.struct.primitive.impl;

import net.andreho.struct.Cleanable;
import net.andreho.struct.Measurable;
import net.andreho.struct.Reservable;

/**
 * Created by 666 on 07.08.2016.
 */
public abstract class AbstractPrimitiveMap implements Cleanable, Reservable, Measurable {
   protected int size;

   @Override
   public int size() {
      return size;
   }

   @Override
   public boolean isEmpty() {
      return size() == 0;
   }
}
