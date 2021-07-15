
package one.microstream.demo.bookstore.data;

import java.util.Objects;

/**
 * Genre entity which holds a name and a {@link Country}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class State extends Named
{
	private final Country country;

	/**
	 * Constructor method to create a new {@link State} instance.
	 *
	 * @param name not empty
	 * @param country not <code>null</code>
	 */
	public State(
		final String  name   ,
		final Country country
	)
	{
		super(name);
		
		this.country = Objects.requireNonNull(country, () -> "Country cannot be null");
	}
	
	/**
	 * Get the country.
	 *
	 * @return the country
	 */
	public Country country()
	{
		return this.country;
	}

}
