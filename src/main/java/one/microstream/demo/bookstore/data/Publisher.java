
package one.microstream.demo.bookstore.data;

/**
 * Publisher entity which holds a name and an {@link Address}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Publisher extends NamedWithAddress
{
	/**
	 * Constructor to create a new {@link Publisher} instance.
	 *
	 * @param name not empty
	 * @param address not <code>null</code>
	 */
	public Publisher(
		final String  name   ,
		final Address address
	)
	{
		super(name, address);
	}

}
