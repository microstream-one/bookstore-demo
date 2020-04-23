
package one.microstream.readmecorp.data;

public interface Customer extends NamedWithAddress
{
	public static Customer New(
		final String name,
		final Address address
	)
	{
		return new Default(name, address);
	}
	
	public static class Default extends NamedWithAddress.Abstract implements Customer
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
