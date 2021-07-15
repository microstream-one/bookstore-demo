
package one.microstream.demo.bookstore.data;

/**
 * Country entity which holds a name and an ISO 3166 2-letter country code.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Country extends NamedWithCode
{
	public Country(
		final String name,
		final String code
	)
	{
		super(name, code);
	}

}
