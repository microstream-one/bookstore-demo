
package one.microstream.demo.bookstore.data;

/**
 * Feature type for all named entities with a code.
 *
 */
public interface NamedWithCode extends Named
{
	/**
	 * Get the code.
	 *
	 * @return the code
	 */
	public String code();


	/**
	 * Abstract implementation of the {@link NamedWithCode} interface.
	 *
	 */
	public static abstract class Abstract extends Named.Abstract implements NamedWithCode
	{
		private final String code;

		Abstract(
			final String name,
			final String code
		)
		{
			super(name);
			this.code = code;
		}

		@Override
		public String code()
		{
			return this.code;
		}

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName() + " [" + this.name() + " - " + this.code + "]";
		}

	}

}
