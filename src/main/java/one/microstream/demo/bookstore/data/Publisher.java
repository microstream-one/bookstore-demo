
package one.microstream.demo.bookstore.data;

public interface Publisher extends NamedWithAddress
{
	public static Publisher New(
		final String name,
		final Address address
	)
	{
		return new Default(name, address);
	}


	public static class Default extends NamedWithAddress.Abstract implements Publisher
	{
		public Default(
			final String name,
			final Address address
		)
		{
			super(name, address);
		}

	}

}
