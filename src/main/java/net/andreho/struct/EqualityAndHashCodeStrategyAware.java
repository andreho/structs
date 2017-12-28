package net.andreho.struct;

public interface EqualityAndHashCodeStrategyAware<T> {
   EqualityStrategy<T> equalityStrategy();
   HashCodeStrategy<T> hashCodeStrategy();
}
