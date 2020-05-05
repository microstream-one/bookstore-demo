
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.data.Named.Validation.validateName;

import java.util.Objects;

/**
 * Genre entity which holds a name and a {@link Country}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface State extends Named
{
	/**
	 * Get the country.
	 *
	 * @return the country
	 */
	public Country country();


	/**
	 * Pseudo-constructor method to create a new {@link State} instance with default implementation.
	 *
	 * @param name not empty, {@link Named.Validation#validateName(String)}
	 * @param country not <code>null</code>
	 * @return a new {@link State} instance
	 */
	public static State New(
		final String name,
		final Country country
	)
	{
		return new Default(
			validateName(name),
			Objects.requireNonNull(country, () -> "Country cannot be null")
		);
	}


	/**
	 * Default implementation of the {@link State} interface.
	 *
	 */
	public static class Default extends Named.Abstract implements State
	{
		private final Country country;

		Default(
			final String name,
			final Country country
		)
		{
			super(name);
			this.country = country;
		}

		@Override
		public Country country()
		{
			return this.country;
		}

	}

}
