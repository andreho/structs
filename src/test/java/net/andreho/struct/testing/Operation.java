package net.andreho.struct.testing;

public interface Operation {
   void execute();
   void undo();

   boolean isExecuted();
   boolean isUnDid();
}
