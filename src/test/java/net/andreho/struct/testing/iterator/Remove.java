package net.andreho.struct.testing.iterator;

import net.andreho.struct.testing.abstr.AbstractVerifiableOperation;

import java.util.Objects;

public class Remove extends AbstractVerifiableOperation {
   @FunctionalInterface
   public interface Semantic {
      void execute();
   }
   @FunctionalInterface
   public interface Verification<T> {
      boolean verify(T value);
   }

   final Next next;
   final Remove.Semantic testedRemove;
   final Remove.Semantic verifiableRemove;
   final Remove.Verification testedVerification;
   final Remove.Verification verifiableVerification;

   Exception testedException;
   Exception verifiableException;

   public Remove(final Next next,
                 final Remove.Semantic testedRemove,
                 final Remove.Semantic verifiableRemove,
                 final Remove.Verification<?> testedVerification,
                 final Remove.Verification<?> verifiableVerification) {
      this.next = next;
      this.testedRemove = testedRemove;
      this.verifiableRemove = verifiableRemove;
      this.testedVerification = testedVerification;
      this.verifiableVerification = verifiableVerification;
   }

   @Override
   protected boolean verifyInternally() {
      return Objects.equals(testedException, verifiableException) &&
             testedVerification.verify(next.getTestedResult()) ==
             verifiableVerification.verify(next.getVerifiableResult());
   }

   @Override
   protected void executeInternally() {
      try {
         testedRemove.execute();
      } catch (Exception e) {
         testedException = e;
      }
      try {
         verifiableRemove.execute();
      } catch (Exception e) {
         verifiableException = e;
      }
   }
}
