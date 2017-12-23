package net.andreho.struct.entry;

import java.util.Map.Entry;

/**
 * <br/>Created by a.hofmann on 08.02.2016.<br/>
 */
public class Pair<K, V> implements Entry<K, V> {
   private volatile K key;
   private volatile V value;

   public Pair() {
   }

   public Pair(K key) {
      this.key = key;
   }

   //----------------------------------------------------------------------------------------------------------------

   public Pair(K key, V value) {
      this.key = key;
      this.value = value;
   }

   public static <A, B> Pair<A, B> of(A key, B value) {
      return new Pair<>(key, value);
   }

   //----------------------------------------------------------------------------------------------------------------

   @Override
   public K getKey() {
      return key;
   }

   public void setKey(K key) {
      this.key = key;
   }

   @Override
   public V getValue() {
      return value;
   }

   @Override
   public V setValue(V value) {
      V old = this.value;
      this.value = value;
      return old;
   }

   //----------------------------------------------------------------------------------------------------------------

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      Pair<?, ?> pair = (Pair<?, ?>) o;

      if (key != null ? !key.equals(pair.key) : pair.key != null) {
         return false;
      }
      return value != null ? value.equals(pair.value) : pair.value == null;
   }

   @Override
   public int hashCode() {
      int result = key != null ? key.hashCode() : 0;
      result = 31 * result + (value != null ? value.hashCode() : 0);
      return result;
   }
}
