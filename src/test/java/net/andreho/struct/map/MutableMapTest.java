package net.andreho.struct.map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.andreho.struct.map.MutableMap.linearProbingMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by a.hofmann on 02.07.2016.
 */
public class MutableMapTest extends DataSets {
   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void get(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      for(Integer key : integers) {
         assertEquals(key, map.get(key));
         assertEquals(key, hashMap.get(key));
      }

      assertEquals(map.getDefaultValue(), map.get(findNotPresent(hashMap)));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void getOrDefault(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      for(Integer key : integers) {
         assertEquals(key, map.getOrDefault(key, 0));
         assertEquals(key, hashMap.getOrDefault(key, 0));
      }

      assertEquals(0, map.getOrDefault(findNotPresent(hashMap), 0).intValue());
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void containsKey(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      for(Integer key : integers) {
         assertEquals(hashMap.containsKey(key), map.containsKey(key));
      }

      assertFalse(map.containsKey(findNotPresent(hashMap)));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void containsValue(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      for(Integer key : integers) {
         assertEquals(hashMap.containsValue(key), map.containsValue(key));
      }

      assertFalse(map.containsValue(findNotPresent(hashMap)));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void keySetContainsAll(final List<Integer> integers) {
      MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);
      assertTrue(map.keySet().containsAll(integers));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void valuesContainsAll(final List<Integer> integers) {
      MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);
      assertTrue(map.values().containsAll(integers));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void entrySetContainsAll(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      assertTrue(map.toMap().entrySet().containsAll(hashMap.entrySet()));
      assertTrue(hashMap.entrySet().containsAll(map.toMap().entrySet()));
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void equals(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      assertEquals(hashMap, map.toMap());
      assertEquals(map.toMap(), hashMap);
   }

   @ParameterizedTest(name = "#{index}")
   @MethodSource({ONE,FIVE,TEN,HUNDRED,THOUSAND,TEN_THOUSAND})
   void hashCode(final List<Integer> integers) {
      final Map<Integer, Integer> hashMap = fillMap(new HashMap<>(), integers);
      final MutableMap<Integer, Integer> map = fillMap(linearProbingMap(), integers);

      int expected = hashMap.hashCode();
      int actual = map.toMap().hashCode();

      assertEquals(expected, actual);
   }

   private static MutableMap<Integer, Integer> fillMap(final MutableMap<Integer, Integer> map, final Collection<Integer> integers) {
      for (Integer integer : integers) {
         map.put(integer, integer);
      }
      return map;
   }

   private static Map<Integer, Integer> fillMap(final Map<Integer, Integer> map, final Collection<Integer> integers) {
      for (Integer integer : integers) {
         map.put(integer, integer);
      }
      return map;
   }

   private static Integer findNotPresent(final Map<Integer, Integer> map) {
      for(int key = 0; key < Integer.MAX_VALUE; key++) {
         if(!map.containsKey(key)) {
            return key;
         }
      }
      return Integer.MIN_VALUE;
   }
}