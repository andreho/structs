package net.andreho.struct.map.impl;

import net.andreho.struct.map.MutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by a.hofmann on 07.07.2016.
 */
public class LinearProbingHashMapTest {

   //-----------------------------------------------------------------------------------------------------------------

   private static final int TESTS = 1000000;
   private static final int AMOUNT = 1000000;
   private static final int MISSING_COUNT = (int) (AMOUNT * 0.25);

   //-----------------------------------------------------------------------------------------------------------------

   private List<Integer> any = new ArrayList<>();
   private List<Integer> keys = new ArrayList<>();
   private Map<Integer, Integer> map =
//         new HashMap<>();
         MutableMap.<Integer, Integer>linearProbingMap().toMap();

   //-----------------------------------------------------------------------------------------------------------------

   private static long measureTime(int times, Runnable operation) {
      long time = System.nanoTime();

      for (int i = 0; i < times; i++) {
         operation.run();
      }

      return System.nanoTime() - time;
   }

   //-----------------------------------------------------------------------------------------------------------------

   private void generateData() {
      final ThreadLocalRandom random = ThreadLocalRandom.current();

      for (int i = 0; i < AMOUNT; i++) {
         final Integer key = random.nextInt();
         any.add(key);
         keys.add(key);
         map.put(key, key);
      }

      for (int i = 0; i < MISSING_COUNT; i++) {
         any.add(random.nextInt());
      }

      Collections.shuffle(any);
      Collections.shuffle(keys);
   }

   @Before
   public void prepareData() {
      generateData();
   }

   @Test
   public void measure_get() {
      for (int i = 0; i < 1000; i++) {
         Iterator<Integer> iterator = keys.iterator();
         System.out.println("time for get: " + measureTime(TESTS, () -> {
            map.get(iterator.next());
         }) / (double) TESTS);
      }
   }

   @Test
   public void measure_put() {

   }

   @Test
   public void measure_remove() {

   }
}