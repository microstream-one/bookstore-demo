
package one.microstream.demo.bookstore;

public interface Country extends NamedWithCode
{
	public static Country New(
		final String name,
		final String code
	)
	{
		return new Default(name, code);
	}
	
	public static class Default extends NamedWithCode.Abstract implements Country
	{
		Default(
			final String name,
			final String code
		)
		{
			super(name, code);
		}
		
	}
	
}
