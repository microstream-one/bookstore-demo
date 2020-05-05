package one.microstream.demo.bookstore.util.concurrent;

/**
 * Operation with no return value, used by {@link ReadWriteLocked} and {@link ReadWriteLockedStriped}.
 *
 */
@FunctionalInterface
public interface VoidOperation
{
	/**
	 * Execute an arbitrary operation
	 */
	public void execute();
}