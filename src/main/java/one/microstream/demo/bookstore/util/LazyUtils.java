package one.microstream.demo.bookstore.util;

import java.util.Optional;

import one.microstream.reference.Lazy;

/**
 * {@link Lazy} reference utilities
 *
 */
public interface LazyUtils
{
	/**
	 * Clears a {@link Lazy} reference if it is not <code>null</code> and stored.
	 *
	 * @param <T>
	 * @param lazy the reference, may be <code>null</code>
	 * @return the optional content of the lazy reference when it was cleared successfully
	 * @see Lazy#isStored()
	 * @see Lazy#clear()
	 */
	public static <T> Optional<T> clearIfStored(final Lazy<T> lazy)
	{
		return lazy != null && lazy.isStored()
			? Optional.ofNullable(lazy.clear())
			: Optional.empty()
		;
	}
}
