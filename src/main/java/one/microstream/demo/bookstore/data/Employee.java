
package one.microstream.demo.bookstore.data;

/**
 * Employee entity which holds a name and an {@link Address}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Employee extends NamedWithAddress
{
	/**
	 * Constructor to create a new {@link Employee} instance.
	 *
	 * @param name not empty
	 * @param address not <code>null</code>
	 */
	public Employee(
		final String  name   ,
		final Address address
	)
	{
		super(name, address);
	}

}
