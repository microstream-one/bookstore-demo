package one.microstream.demo.bookstore.util;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Validation utilities
 *
 */
public interface ValidationUtils
{
	/**
	 * Checks that the specified char sequence is not {@code null} or empty and throws a customized
	 * {@link NullPointerException} respectively {@link IllegalArgumentException} if it is.
     *
	 * @param <T>
	 * @param charSequence the char sequence to check
	 * @param messageSupplier supplier of the detail message to be
	 * used in the event that a exception is thrown
	 * @return {@code charSequence} if not {@code null} or empty
	 * @throws NullPointerException if {@code charSequence} is {@code null}
	 * @throws IllegalArgumentException if {@code charSequence} is empty
	 */
	public static <T extends CharSequence> T requireNonEmpty(
		final T charSequence,
		final Supplier<String> messageSupplier
	)
	{
		if(charSequence == null)
		{
			throw new NullPointerException(messageSupplier.get());
		}
		if(charSequence.length() == 0)
		{
			throw new IllegalArgumentException(messageSupplier.get());
		}
        return charSequence;
	}

	/**
	 * Checks that the specified char sequence is not {@code null} or blank and throws a customized
	 * {@link NullPointerException} respectively {@link IllegalArgumentException} if it is.
     *
	 * @param <T>
	 * @param charSequence the char sequence to check
	 * @param messageSupplier supplier of the detail message to be
	 * used in the event that a exception is thrown
	 * @return {@code charSequence} if not {@code null} or blank
	 * @throws NullPointerException if {@code charSequence} is {@code null}
	 * @throws IllegalArgumentException if {@code charSequence} is blank
	 */
	public static <T extends CharSequence> T requireNonBlank(
		final T charSequence,
		final Supplier<String> messageSupplier
	)
	{
		requireNonEmpty(charSequence, messageSupplier);
		for(int i = 0, c = charSequence.length(); i < c; i++)
		{
			if(!Character.isWhitespace(charSequence.charAt(i)))
			{
				return charSequence;
			}
		}
		throw new IllegalArgumentException(messageSupplier.get());
	}

	/**
	 * Checks that the specified collection is not {@code null} or empty and throws a customized
	 * {@link NullPointerException} respectively {@link IllegalArgumentException} if it is.
     *
	 * @param <T>
	 * @param collection the collection to check
	 * @param messageSupplier supplier of the detail message to be
	 * used in the event that a exception is thrown
	 * @return {@code collection} if not {@code null} or empty
	 * @throws NullPointerException if {@code collection} is {@code null}
	 * @throws IllegalArgumentException if {@code collection} is empty
	 */
	public static <E, T extends Collection<E>> T requireNonEmpty(
		final T collection,
		final Supplier<String> messageSupplier
	)
	{
		if(collection == null)
		{
			throw new NullPointerException(messageSupplier.get());
		}
		if(collection.isEmpty())
		{
			throw new IllegalArgumentException(messageSupplier.get());
		}
        return collection;
	}

	/**
	 * Checks that the specified value is greater than zero and throws a customized
	 * {@link IllegalArgumentException} if it isn't.
     *
	 * @param value the value to check
	 * @param messageSupplier supplier of the detail message to be
	 * used in the event that a exception is thrown
	 * @return {@code value} if greater than zero
	 * @throws IllegalArgumentException if {@code value} is zero or less
	 */
	public static int requirePositive(
		final int value,
		final Supplier<String> messageSupplier
	)
	{
		if(value <= 0)
		{
			throw new IllegalArgumentException(messageSupplier.get());
		}
        return value;
	}

	/**
	 * Checks that the specified value is greater or equal than zero and throws a customized
	 * {@link IllegalArgumentException} if it isn't.
     *
	 * @param value the value to check
	 * @param messageSupplier supplier of the detail message to be
	 * used in the event that a exception is thrown
	 * @return {@code value} if zero or more
	 * @throws IllegalArgumentException if {@code value} less than zero
	 */
	public static int requireZeroOrPositive(
		final int value,
		final Supplier<String> messageSupplier
	)
	{
		if(value < 0)
		{
			throw new IllegalArgumentException(messageSupplier.get());
		}
        return value;
	}
}
