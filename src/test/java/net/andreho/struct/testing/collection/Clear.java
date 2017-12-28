package net.andreho.struct.testing.collection;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.function.IntSupplier;

public class Clear extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic {
      void execute();
   }

   final Semantic tested;
   final Semantic verifiable;

   final IntSupplier testedSizeSupplier;
   final IntSupplier verifiableSizeSupplier;

   public Clear(final Semantic testedClear,
                final Semantic verifiableClear,
                final IntSupplier testedSizeSupplier,
                final IntSupplier verifiableSizeSupplier) {
      this.tested = testedClear;
      this.verifiable = verifiableClear;
      this.testedSizeSupplier = testedSizeSupplier;
      this.verifiableSizeSupplier = verifiableSizeSupplier;
   }

   @Override
   protected boolean verifyInternally() {
      final int testedSize = testedSizeSupplier.getAsInt();
      final int verifiableSize = verifiableSizeSupplier.getAsInt();
      return testedSize == verifiableSize;
   }

   @Override
   protected void executeInternally() {
      tested.execute();
      verifiable.execute();
   }

   @Override
   protected void undoInternally() {
   }
}
