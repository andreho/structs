package net.andreho.struct.testing;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.function.IntSupplier;

public class Sandbox {
   private int a(){
      return 1;
   }
   private int b(){
      return 2;
   }
   @Test
   void test() {
      final IntSupplier a1 = getA();
      final IntSupplier a2 = this::a; //getA();

      final IntSupplier b1 = getB();
      final IntSupplier b2 = getB();

      //True
      System.out.println("a1.equals(a1): "+a1.equals(a1));
      System.out.println("a1.equals(a2): "+a1.equals(a2));
      System.out.println("Equal classes: "+a1.getClass().equals(a2.getClass()));

      printMemberFields(a1);
      printMemberFields(a2);

      //False
      System.out.println(a1.equals(b1));
      System.out.println(a1.equals(b2));
   }

   private IntSupplier getB() {
      return this::b;
   }

   private IntSupplier getA() {
      return this::a;
   }

   static void printMemberFields(Object o) {
      System.out.println("Fields of ("+o.getClass().getName()+"):--------------------------------------------");
      for(Field field : o.getClass().getDeclaredFields()) {
         System.out.println(field);
      }
      System.out.println(
         "----------------------------------------------------" +
         "--------------------------------------------------------------");
   }
}
