package net.andreho.struct.map;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;

public class DataSets {
   protected static final String ONE_RANDOM = "oneRandom";
   protected static final String FIVE_RANDOM = "fiveRandoms";
   protected static final String TEN_RANDOM = "tenRandoms";
   protected static final String HUNDRED_RANDOM = "hundredRandoms";
   protected static final String THOUSAND_RANDOM = "thousandRandoms";
   protected static final String TEN_THOUSAND_RANDOM = "tenThousandRandoms";
   protected static final String HUNDRED_THOUSAND_RANDOM = "hundredThousandRandoms";

   protected static final String ONE = "one";
   protected static final String FIVE = "five";
   protected static final String TEN = "ten";
   protected static final String HUNDRED = "hundred";
   protected static final String THOUSAND = "thousand";
   protected static final String TEN_THOUSAND = "tenThousand";
   protected static final String HUNDRED_THOUSAND = "hundredThousand";

   protected static List<List<Integer>> oneRandom() {
      return singletonList(
         current()
         .ints(1)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> fiveRandoms() {
      return singletonList(
         current()
         .ints(5)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> tenRandoms() {
      return singletonList(
         current()
         .ints(10)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> hundredRandoms() {
      return singletonList(
         current()
         .ints(100)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> thousandRandoms() {
      return singletonList(
         current()
         .ints(1000)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> tenThousandRandoms() {
      return singletonList(
         current()
         .ints(10_000)
         .boxed()
         .collect(toList())
      );
   }

   protected static List<List<Integer>> hundredThousandRandoms() {
      return singletonList(
         current()
         .ints(100_000)
         .boxed()
         .collect(toList())
      );
   }

   private static final List<Integer> INTEGERS =
      current()
      .ints(100_000)
      .distinct()
      .boxed()
      .collect(Collectors.toList());

   protected static List<List<Integer>> one() {
      return singletonList(INTEGERS.subList(0, 1));
   }

   protected static List<List<Integer>> five() {
      return singletonList(INTEGERS.subList(0, 5));
   }

   protected static List<List<Integer>> ten() {
      return singletonList(INTEGERS.subList(0, 10));
   }

   protected static List<List<Integer>> hundred() {
      return singletonList(INTEGERS.subList(0, 100));
   }

   protected static List<List<Integer>> thousand() {
      return singletonList(INTEGERS.subList(0, 1000));
   }

   protected static List<List<Integer>> tenThousand() {
      return singletonList(INTEGERS.subList(0, 10_000));
   }

   protected static List<List<Integer>> hundredThousand() {
      return singletonList(INTEGERS.subList(0, 100_000));
   }
}
