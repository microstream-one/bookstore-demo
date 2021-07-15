
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requirePositive;

/**
 * Customer entity which holds a customer id, name and an {@link Address}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Customer extends NamedWithAddress
{
	private final int customerId;
	
	/**
	 * Constructor method to create a new {@link Customer} instance.
	 *
	 * @param customerId positive customer id
	 * @param name not empty
	 * @param address not <code>null</code>
	 */
	public Customer(
		final int     customerId,
		final String  name      ,
		final Address address
	)
	{
		super(name, address);
		
		this.customerId = requirePositive(customerId, () -> "Customer id must be positive");
	}
	
	/**
	 * Get the customer id
	 *
	 * @return the customer id
	 */
	public int customerId()
	{
		return this.customerId;
	}
	
}
