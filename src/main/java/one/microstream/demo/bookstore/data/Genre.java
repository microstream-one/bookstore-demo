
package one.microstream.demo.bookstore.data;

public interface Genre extends Named
{
	public static Genre New(
		final String name
	)
	{
		return new Default(name);
	}


	public static class Default extends Named.Abstract implements Genre
	{
		Default(
			final String name
		)
		{
			super(name);
		}

	}

}
