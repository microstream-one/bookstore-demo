
package one.microstream.demo.bookstore.data;

import java.util.Objects;

/**
 * Feature type for all named entities with an {@link Address}.
 *
 */
public interface NamedWithAddress extends Named
{
	/**
	 * Get the address of this entity.
	 *
	 * @return the address
	 */
	public Address address();


	/**
	 * Validation utilities for the {@link NamedWithAddress} type.
	 *
	 */
	public static interface Validation
	{
		/**
		 * Validates the given address.
		 * It is returned if not <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param address the address to validate
		 * @return the given address
		 * @throws NullPointerException if the given name is <code>null</code>
		 */
		public static Address validateAddress(final Address address)
		{
			return Objects.requireNonNull(address, () -> "Address cannot be null");
		}
	}


	/**
	 * Abstract implementation of the {@link NamedWithAddress} interface.
	 *
	 */
	public static class Abstract extends Named.Abstract implements NamedWithAddress
	{
		private final Address address;

		Abstract(
			final String name,
			final Address address
		)
		{
			super(name);
			this.address = address;
		}

		@Override
		public Address address()
		{
			return this.address;
		}

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName() + " [" + this.name() + " - " + this.address + "]";
		}

	}

}
