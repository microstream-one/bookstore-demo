
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.data.Named.Validation.validateName;
import static one.microstream.demo.bookstore.data.NamedWithAddress.Validation.validateAddress;

/**
 * Publisher entity which holds a name and an {@link Address}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Publisher extends NamedWithAddress
{
	/**
	 * Pseudo-constructor method to create a new {@link Publisher} instance with default implementation.
	 *
	 * @param name not empty, {@link Named.Validation#validateName(String)}
	 * @param address not <code>null</code>
	 * @return a new {@link Publisher} instance
	 */
	public static Publisher New(
		final String name,
		final Address address
	)
	{
		return new Default(
			validateName(name),
			validateAddress(address)
		);
	}


	/**
	 * Default implementation of the {@link Publisher} interface.
	 *
	 */
	public static class Default extends NamedWithAddress.Abstract implements Publisher
	{
		public Default(
			final String name,
			final Address address
		)
		{
			super(name, address);
		}

	}

}
