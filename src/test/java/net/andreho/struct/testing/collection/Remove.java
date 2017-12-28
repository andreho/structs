package net.andreho.struct.testing.collection;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Collection;

/**
 * Contract for {@link Collection#add(Object)} <br/>
 * @param <E>
 */
public class Remove<E> extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic<E> {
      boolean execute(E element);
   }
   boolean testedResult;
   boolean verifiableResult;

   final Semantic<E> testedRemove;
   final Semantic<E> verifiableRemove;

   final Add.Semantic<E> testedAdd;
   final Add.Semantic<E> verifiableAdd;

   final E element;

   public Remove(final Semantic<E> testedRemove,
                 final Semantic<E> verifiableRemove,
                 final Add.Semantic<E> testedAdd,
                 final Add.Semantic<E> verifiableAdd,
                 final E element) {
      this.testedRemove = testedRemove;
      this.verifiableRemove = verifiableRemove;

      this.testedAdd = testedAdd;
      this.verifiableAdd = verifiableAdd;

      this.element = element;
   }

   @Override
   protected boolean verifyInternally() {
      return testedResult == verifiableResult;
   }

   @Override
   protected void executeInternally() {
      this.testedResult = testedRemove.execute(element);
      this.verifiableResult = verifiableRemove.execute(element);
   }

   @Override
   protected void undoInternally() {
      if(testedResult) {
         this.testedResult = testedAdd.execute(element);
      }
      if(verifiableResult) {
         this.verifiableResult = verifiableRemove.execute(element);
      }
   }
}
