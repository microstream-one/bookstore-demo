
package one.microstream.demo.bookstore.data;

public interface State extends Named
{
	public Country country();


	public static State New(
		final String name,
		final Country country
	)
	{
		return new Default(name, country);
	}


	public static class Default extends Named.Abstract implements State
	{
		private final Country country;

		Default(
			final String name,
			final Country country
		)
		{
			super(name);
			this.country = country;
		}

		@Override
		public Country country()
		{
			return this.country;
		}

	}

}
