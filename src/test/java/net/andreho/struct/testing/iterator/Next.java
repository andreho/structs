package net.andreho.struct.testing.iterator;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Objects;

public class Next extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic<T> {
      T execute();
   }

   final HasNext hasNext;
   final Next.Semantic testedNext;
   final Next.Semantic verifiableNext;

   Object testedResult;
   Object verifiableResult;

   public Next(final HasNext hasNext,
               final Next.Semantic testedNext,
               final Next.Semantic verifiableNext) {
      this.hasNext = hasNext;
      this.testedNext = testedNext;
      this.verifiableNext = verifiableNext;
   }

   public Object getTestedResult() {
      return testedResult;
   }

   public Object getVerifiableResult() {
      return verifiableResult;
   }

   public boolean completedUnexpectedly() {
      return testedResult instanceof Exception ||
             verifiableResult instanceof Exception;
   }

   @Override
   protected boolean verifyInternally() {
      hasNext.verify();
      return Objects.equals(testedResult, verifiableResult);
   }

   @Override
   protected void executeInternally() {
      hasNext.execute();
      try {
         testedResult = testedNext.execute();
      } catch (Exception e) {
         testedResult = e;
      }
      try {
         verifiableResult = verifiableNext.execute();
      } catch (Exception e) {
         verifiableResult = e;
      }
   }
}
