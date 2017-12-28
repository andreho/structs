package net.andreho.struct.testing.collection;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Collection;

/**
 * Contract for {@link Collection#add(Object)} <br/>
 * @param <E>
 */
public class Add<E> extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic<E> {
      boolean execute(E value);
   }

   boolean testedResult;
   boolean verifiableResult;

   final Add.Semantic<E> testedAdd;
   final Add.Semantic<E> verifiableAdd;

   final Remove.Semantic<E> testedRemove;
   final Remove.Semantic<E> verifiableRemove;

   final E element;

   public Add(
      final Semantic<E> testedAdd,
      final Semantic<E> verifiableAdd,
      final Remove.Semantic<E> testedRemove,
      final Remove.Semantic<E> verifiableRemove,
      final E element) {
      this.testedAdd = testedAdd;
      this.verifiableAdd = verifiableAdd;

      this.testedRemove = testedRemove;
      this.verifiableRemove = verifiableRemove;

      this.element = element;
   }

   @Override
   protected boolean verifyInternally() {
      return testedResult == verifiableResult;
   }

   @Override
   protected void executeInternally() {
      this.testedResult = testedAdd.execute(element);
      this.verifiableResult = verifiableAdd.execute(element);
   }

   @Override
   protected void undoInternally() {
      if(testedResult) {
         testedResult = testedRemove.execute(element);
      }
      if(verifiableResult) {
         verifiableResult = verifiableRemove.execute(element);
      }
   }
}
