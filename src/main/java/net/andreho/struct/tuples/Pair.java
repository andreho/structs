package net.andreho.struct.tuples;

import java.util.Objects;

/**
 * <br/>Created by a.hofmann on 08.02.2016.<br/>
 */
public class Pair<A, B> {
   private volatile A first;
   private volatile B second;

   public Pair() {
   }

   public Pair(A first) {
      this.first = first;
   }

   public Pair(A first, B second) {
      this.first = first;
      this.second = second;
   }

   public static <A, B> Pair<A, B> of(A first, B second) {
      return new Pair<>(first, second);
   }

   public A getFirst() {
      return first;
   }

   public void setFirst(A first) {
      this.first = first;
   }

   public B getSecond() {
      return second;
   }

   public void setSecond(B value) {
      this.second = value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Pair)) {
         return false;
      }
      Pair that = (Pair) o;
      return Objects.equals(first, that.first) &&
             Objects.equals(second, that.second);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(first) * 31 +
             Objects.hashCode(second);
   }
}
