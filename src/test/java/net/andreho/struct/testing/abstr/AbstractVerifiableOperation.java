package net.andreho.struct.testing.abstr;

import net.andreho.struct.testing.Verifiable;

public abstract class AbstractVerifiableOperation extends AbstractOperation implements Verifiable {
   private boolean verified;

   @Override
   public void execute() {
      super.execute();
      this.verified = false;
   }

   @Override
   public void undo() {
      super.undo();
      this.verified = false;
   }

   /**
    * @return <b>true</b> if verification fails
    */
   @Override
   public boolean verify() {
      this.verified = true;
      return verifyInternally();
   }

   @Override
   public boolean isVerified() {
      return verified;
   }

   protected abstract boolean verifyInternally();
}
