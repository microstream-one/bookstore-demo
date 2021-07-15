
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonBlank;

/**
 * Feature type for all named entities, with {@link Comparable} capabilities.
 *
 */
public abstract class Named implements Comparable<Named>
{
	private final String name;

	protected Named(final String name)
	{
		super();
		
		this.name = requireNonBlank(name, () -> "Name cannot be empty");
	}

	/**
	 * Get the name of this entity.
	 *
	 * @return the name
	 */
	public String name()
	{
		return this.name;
	}

	@Override
	public int compareTo(final Named other)
	{
		return this.name().compareTo(other.name());
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + " [" + this.name + "]";
	}

}
