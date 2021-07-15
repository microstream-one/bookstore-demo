
package one.microstream.demo.bookstore.data;

/**
 * Genre entity which holds a name.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Genre extends Named
{
	/**
	 * Constructor method to create a new {@link Genre} instance.
	 *
	 * @param name not empty
	 */
	public Genre(
		final String name
	)
	{
		super(name);
	}

}
