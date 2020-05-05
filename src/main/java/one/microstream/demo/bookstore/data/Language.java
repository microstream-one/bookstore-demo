
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;

import java.util.Locale;


/**
 * Language entity which holds a {@link Locale}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Language extends Named
{
	/**
	 * Get the locale.
	 *
	 * @return the locale
	 */
	public Locale locale();

	/**
	 * Get the display language of {@link #locale()}.
	 *
	 * @return the locale's display language
	 * @see Locale#getDisplayLanguage()
	 */
	@Override
	public default String name()
	{
		return this.locale().getDisplayLanguage();
	}


	/**
	 * Pseudo-constructor method to create a new {@link Language} instance with default implementation.
	 *
	 * @param locale not <code>null</code>
	 * @return a new {@link Language} instance
	 */
	public static Language New(
		final Locale locale
	)
	{
		return new Default(
			requireNonNull(locale, () -> "Locale cannot be null")
		);
	}


	/**
	 * Default implementation of the {@link Language} interface.
	 *
	 */
	public static class Default implements Language
	{
		private final Locale locale;

		Default(
			final Locale locale
		)
		{
			super();
			this.locale = locale;
		}

		@Override
		public Locale locale()
		{
			return this.locale;
		}

		@Override
		public String toString()
		{
			return "Language [" + this.name() + "]";
		}

	}

}
