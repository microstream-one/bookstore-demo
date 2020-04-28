
package one.microstream.demo.bookstore.data;

public interface Author extends NamedWithAddress
{
	public static Author New(
		final String name,
		final Address address
	)
	{
		return new Default(name, address);
	}
	
	public static class Default extends NamedWithAddress.Abstract implements Author
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
