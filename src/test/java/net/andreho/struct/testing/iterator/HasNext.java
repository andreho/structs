package net.andreho.struct.testing.iterator;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

public class HasNext extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic {
      boolean execute();
   }

   final HasNext.Semantic testedHasNext;
   final HasNext.Semantic verifiableHasNext;

   boolean testedResult;
   boolean verifiableResult;

   public HasNext(final Semantic testedHasNext, final Semantic verifiableHasNext) {
      this.testedHasNext = testedHasNext;
      this.verifiableHasNext = verifiableHasNext;
   }

   @Override
   protected boolean verifyInternally() {
      return testedResult == verifiableResult;
   }

   @Override
   protected void executeInternally() {
      this.testedResult = testedHasNext.execute();
      this.verifiableResult = verifiableHasNext.execute();
   }
}
