
package one.microstream.demo.bookstore.data;

public interface Named extends Entity, Comparable<Named>
{
	public String name();

	@Override
	public default int compareTo(final Named other)
	{
		return this.name().compareTo(other.name());
	}


	public static abstract class Abstract implements Named
	{
		private final String name;

		Abstract(
			final String name
		)
		{
			super();
			this.name = name;
		}

		@Override
		public String name()
		{
			return this.name;
		}

		@Override
		public String toString()
		{
			return this.getClass().getSimpleName() + " [" + this.name + "]";
		}

	}

}
