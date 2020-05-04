
package one.microstream.demo.bookstore.data;

public interface Customer extends NamedWithAddress
{
	public int customerId();


	public static Customer New(
		final int customerId,
		final String name,
		final Address address
	)
	{
		return new Default(customerId, name, address);
	}


	public static class Default extends NamedWithAddress.Abstract implements Customer
	{
		private final int customerId;

		public Default(
			final int customerId,
			final String name,
			final Address address
		)
		{
			super(name, address);
			this.customerId = customerId;
		}

		@Override
		public int customerId()
		{
			return this.customerId;
		}

	}

}
