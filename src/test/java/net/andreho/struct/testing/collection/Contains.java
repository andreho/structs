package net.andreho.struct.testing.collection;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Collection;

/**
 * Contract for {@link Collection#contains(Object)} <br/>
 * @param <E>
 */
public class Contains<E> extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic<E> {
      boolean execute(E element);
   }

   boolean testedResult;
   boolean verifiableResult;

   final Semantic<E> tested;
   final Semantic<E> verifiable;
   final E element;

   public Contains(final Semantic<E> tested, final Semantic<E> verifiable, final E element) {
      this.tested = tested;
      this.verifiable = verifiable;
      this.element = element;
   }

   @Override
   protected boolean verifyInternally() {
      return testedResult == verifiableResult;
   }

   @Override
   protected void executeInternally() {
      this.testedResult = tested.execute(element);
      this.verifiableResult = verifiable.execute(element);
   }

   @Override
   protected void undoInternally() {
   }
}
