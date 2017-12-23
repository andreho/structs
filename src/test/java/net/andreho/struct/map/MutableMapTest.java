package net.andreho.struct.map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by a.hofmann on 02.07.2016.
 */
public class MutableMapTest {

  //-----------------------------------------------------------------------------------------------------------------

  private static final int AMOUNT = 1000000;
  private static final int TO_REMOVE = (int) (AMOUNT * 0.33);

  //-----------------------------------------------------------------------------------------------------------------

  private List<Integer> keys = new ArrayList<>();

  private Map<Integer, Integer> original = new HashMap<>();
  private MutableMap<Integer, Integer> target = MutableMap.createDefault();

  //-----------------------------------------------------------------------------------------------------------------

  private void generateData(int amount,
                            int toRemove) {
    final ThreadLocalRandom random = ThreadLocalRandom.current();

    System.out.println("Creating and storing keys ...");
    for (int i = 0; i < amount; i++) {
      final Integer key = random.nextInt();
      keys.add(key);
      original.put(key, key);
      target.put(key, key);
    }

    for (int i = 0; i < amount; i++) {
      final Integer key = random.nextInt();
      keys.add(key);
    }

    System.out.println("Shuffle keys ...");
    Collections.shuffle(keys);

    long timeForOriginal = 0;
    long timeForTarget = 0;

    System.out.println("Removing some keys ... ");
    for (int i = 0; i < toRemove; i++) {
      int idx = random.nextInt(keys.size());
      Integer key = keys.get(idx);
      //keys.remove(idx);

      long start = System.nanoTime();
      Integer v1 = original.remove(key);
      timeForOriginal += (System.nanoTime() - start);

      start = System.nanoTime();
      Integer v2 = target.remove(key);
      timeForTarget += (System.nanoTime() - start);

      Assert.assertEquals(v1, v2);
    }
    System.out.println("Target avg.: " + (timeForTarget / (double) toRemove));
    System.out.println("Original avg.: " + (timeForOriginal / (double) toRemove));
    System.out.println("-------------------------------------------------");
  }

  //-----------------------------------------------------------------------------------------------------------------

  @Before
  public void preTest() {
    generateData(AMOUNT, TO_REMOVE);
  }

  @After
  public void postTest() {
    this.keys.clear();
    this.original.clear();
    this.target.clear();
  }

  //-----------------------------------------------------------------------------------------------------------------

  @Test
  public void get()
  throws Exception {
    for (Integer key : keys) {
      boolean equals =
        Objects.equals(original.get(key), target.get(key));
      Assert.assertTrue(equals);
    }
  }

  @Test
  public void containsKey()
  throws Exception {
    for (Integer key : keys) {
      boolean equals =
        original.containsKey(key) == target.containsKey(key);
      Assert.assertTrue(equals);
    }
  }

  @Test
  public void getOrDefault()
  throws Exception {
    for (Integer key : keys) {
      boolean equals =
        Objects.equals(original.getOrDefault(key, Integer.MAX_VALUE),
                       target.getOrDefault(key, Integer.MAX_VALUE));
      Assert.assertTrue(equals);
    }
  }

  @Test
  public void entryViewIterator()
  throws Exception {
    int count = 0;

    for (MutableEntry<Integer, Integer> entry : target.entryView()) {
      final Integer key = entry.getKey();
      final Integer value = entry.getValue();

      Assert.assertEquals(key, value);
      Assert.assertEquals(original.get(key), value);
      count++;
    }

    Assert.assertEquals(count, target.size());
    Assert.assertEquals(count, original.size());
  }


//   @Test
//   public void containsValue() throws Exception {
//      for(Integer key : keys) {
//         boolean equals =
//               original.containsValue(key) ==
//               target.containsValue(key);
//         Assert.assertTrue(equals);
//      }
//   }

  @Test
  public void keySet()
  throws Exception {

  }

  @Test
  public void values()
  throws Exception {

  }

  @Test
  public void entryView()
  throws Exception {

  }

  @Test
  public void hashCode_test()
  throws Exception {

  }

  @Test
  public void equal()
  throws Exception {

  }

  //-----------------------------------------------------------------------------------------------------------------

  @Test
  public void put()
  throws Exception {

  }

  @Test
  public void putIfAbsent()
  throws Exception {

  }

  @Test
  public void remove()
  throws Exception {

  }

  @Test
  public void remove1()
  throws Exception {

  }

  @Test
  public void replace()
  throws Exception {

  }

  @Test
  public void replace1()
  throws Exception {

  }

  @Test
  public void add()
  throws Exception {

  }

  @Test
  public void addAll()
  throws Exception {

  }

  @Test
  public void addAll1()
  throws Exception {

  }

  @Test
  public void putAll()
  throws Exception {

  }

  @Test
  public void putAll1()
  throws Exception {

  }

  @Test
  public void size()
  throws Exception {
    Assert.assertEquals(original.size(), target.size());
  }

  @Test
  public void isEmpty()
  throws Exception {
    Assert.assertEquals(original.isEmpty(), target.isEmpty());
  }

}