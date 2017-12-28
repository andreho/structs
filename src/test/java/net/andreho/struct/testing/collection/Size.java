package net.andreho.struct.testing.collection;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Collection;
import java.util.function.IntSupplier;

/**
 * Contract for {@link Collection#add(Object)} <br/>
 * @param <E>
 */
public class Size<E> extends AbstractVerifiableOperation {
   int testedResult;
   int verifiableResult;

   final AbstractVerifiableOperation delegate;
   final IntSupplier testedFunction;
   final IntSupplier verifiableFunction;

   public Size(final AbstractVerifiableOperation delegate,
               final IntSupplier testedFunction,
               final IntSupplier verifiableFunction) {
      this.delegate = delegate;
      this.testedFunction = testedFunction;
      this.verifiableFunction = verifiableFunction;
   }

   @Override
   protected boolean verifyInternally() {
      return testedResult == verifiableResult;
   }

   @Override
   protected void executeInternally() {
      delegate.execute();

      testedResult = testedFunction.getAsInt();
      verifiableResult = verifiableFunction.getAsInt();
   }

   @Override
   protected void undoInternally() {
      delegate.execute();

      testedResult = testedFunction.getAsInt();
      verifiableResult = verifiableFunction.getAsInt();
   }
}
