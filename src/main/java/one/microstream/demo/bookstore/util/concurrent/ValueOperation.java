package one.microstream.demo.bookstore.util.concurrent;

/**
 * Operation with a return value, used by {@link ReadWriteLocked} and {@link ReadWriteLockedStriped}.
 *
 * @param T the return type
 */
@FunctionalInterface
public interface ValueOperation<T>
{
	/**
	 * Execute an arbitrary operation and return the result
	 *
	 * @return the result of the operation
	 */
	public T execute();
}
