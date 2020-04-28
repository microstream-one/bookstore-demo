
package one.microstream.demo.bookstore.data;

public interface NamedWithAddress extends Named
{
	public Address address();


	public static class Abstract extends Named.Abstract implements NamedWithAddress
	{
		private final Address address;

		Abstract(
			final String name,
			final Address address
		)
		{
			super(name);
			this.address = address;
		}

		@Override
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

}
