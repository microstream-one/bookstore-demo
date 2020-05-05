
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;
import static one.microstream.demo.bookstore.data.Named.Validation.validateName;

/**
 * Country entity which holds a name and an ISO 3166 2-letter country code.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Country extends NamedWithCode
{
	/**
	 * Pseudo-constructor method to create a new {@link Country} instance with default implementation.
	 *
	 * @param name not empty, {@link Named.Validation#validateName(String)}
	 * @param code ISO 3166 2-letter country code, not <code>null</code>
	 * @return a new {@link Country} instance
	 */
	public static Country New(
		final String name,
		final String code
	)
	{
		return new Default(
			validateName(name),
			requireNonNull(code, () -> "Code cannot be null")
		);
	}


	/**
	 * Default implementation of the {@link Country} interface.
	 *
	 */
	public static class Default extends NamedWithCode.Abstract implements Country
	{
		Default(
			final String name,
			final String code
		)
		{
			super(name, code);
		}

	}

}
