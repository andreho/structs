package net.andreho.struct.testing.abstr;

import net.andreho.struct.testing.Operation;

public abstract class AbstractOperation implements Operation {
   private boolean executed;
   private boolean unDid;

   protected abstract void executeInternally();
   protected void undoInternally() { /* NO OP */ }

   @Override
   public void execute() {
      this.executed = true;
      this.unDid = false;
      executeInternally();
   }
   @Override
   public void undo() {
      this.executed = false;
      this.unDid = true;
      undoInternally();
   }
   @Override
   public boolean isExecuted() {
      return executed;
   }
   @Override
   public boolean isUnDid() {
      return unDid;
   }
}
