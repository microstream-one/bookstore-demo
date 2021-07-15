
package one.microstream.demo.bookstore.data;

import java.util.Locale;


/**
 * Language entity which holds a {@link Locale}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Language extends Named
{
	private final Locale locale;
	
	/**
	 * Constructor to create a new {@link Language} instance.
	 *
	 * @param locale not <code>null</code>
	 */
	public Language(final Locale locale)
	{
		super(locale.getDisplayLanguage());
		
		this.locale = locale;
	}
	
	/**
	 * Get the locale.
	 *
	 * @return the locale
	 */
	public Locale locale()
	{
		return this.locale;
	}

}
