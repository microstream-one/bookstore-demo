
package one.microstream.demo.bookstore.data;

import java.util.Objects;

/**
 * Feature type for all named entities with an {@link Address}.
 *
 */
public abstract class NamedWithAddress extends Named
{
	private final Address address;

	protected NamedWithAddress(
		final String name,
		final Address address
	)
	{
		super(name);
		
		this.address = Objects.requireNonNull(address, () -> "Address cannot be null");
	}

	/**
	 * Get the address of this entity.
	 *
	 * @return the address
	 */
	public Address address()
	{
		return this.address;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + " [" + this.name() + " - " + this.address + "]";
	}
	
}
