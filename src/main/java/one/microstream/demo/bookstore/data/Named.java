
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonBlank;

/**
 * Feature type for all named entities, with {@link Comparable} capabilities.
 *
 */
public interface Named extends Comparable<Named>
{
	/**
	 * Get the name of this entity.
	 *
	 * @return the name
	 */
	public String name();

	@Override
	public default int compareTo(final Named other)
	{
		return this.name().compareTo(other.name());
	}


	/**
	 * Validation utilities for the {@link Named} type.
	 *
	 */
	public static interface Validation
	{
		/**
		 * Validates the given name.
		 * It is returned if not empty or <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param name the name to validate
		 * @return the given name
		 * @throws IllegalArgumentException if the given name is empty or <code>null</code>
		 */
		public static String validateName(final String name)
		{
			return requireNonBlank(name, () -> "Name cannot be empty");
		}
	}


	/**
	 * Abstract implementation of the {@link Named} interface.
	 *
	 */
	public static abstract class Abstract implements Named
	{
		private final String name;

		Abstract(
			final String name
		)
		{
			super();
			this.name = name;
		}

		@Override
		public String name()
		{
			return this.name;
		}

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName() + " [" + this.name + "]";
		}

	}

}
