package net.andreho.struct.primitive.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

/**
 * Created by 666 on 10.08.2016.
 */
public class IntObjMapImplTest {
   private static final int SIZE = 1024;

   private IntObjMapImpl<Integer> map = new IntObjMapImpl<>();
   private int[] keys;

   @Before
   public void setUp() throws Exception {
      this.keys = generateData(SIZE);
   }

   private int[] generateData(int size) {
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      final Set<Integer> uniqueSet = new HashSet<>(size);
      int[] keys = new int[size];
      int idx = 0;

      while (uniqueSet.size() < size) {
         int key = random.nextInt();
         if(uniqueSet.add(key)) {
            keys[idx++] = key;
         }
      }
      return keys;
   }

   @Test
   public void put() throws Exception {
      for (int retry = 0; retry < 50; retry++) {
         for (int amount = 10; amount <= 10000; amount *= 10) {
            this.keys = generateData(amount);
            for (int i = 0; i < 10; i++) {
               putTimes(amount, 100);
            }
         }
      }
   }

   private void putTimes(final int amount, final int times) throws Exception {
      long putTime = System.nanoTime();
      for (int i = 0; i < times; i++) {
         this.map = new IntObjMapImpl<>(amount);
         putData();
      }
      putTime = System.nanoTime() - putTime;

      long getTime = System.nanoTime();
      for (int i = 0; i < times; i++) {
         getData();
      }
      getTime = System.nanoTime() - getTime;

      System.out.printf("%8d#Avg.Put in %5.2f ns, Avg.Get in %5.2f ns\n", amount,
                        putTime / ((double) (amount * times)), getTime / ((double) (amount * times)));
   }

   private void getData() {
      for(int key : keys) {
         Integer value = map.get(key);
         assertEquals(null, value);
      }
   }

   private void putData() {
      for(int key : keys) {
         assertEquals(map.getDefaultValue(), map.put(key, null));
      }
   }

   @Test
   public void remove() throws Exception {

   }

   @Test
   public void clear() throws Exception {

   }

   @Test
   public void get() throws Exception {

   }

   @Test
   public void containsKey() throws Exception {

   }

   @Test
   public void containsValue() throws Exception {

   }

   @Test
   public void keySet() throws Exception {

   }

   @Test
   public void values() throws Exception {

   }

   @Test
   public void view() throws Exception {

   }

   @Test
   public void size() throws Exception {

   }

}