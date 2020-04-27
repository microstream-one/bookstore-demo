
package one.microstream.demo.bookstore;

public interface NamedWithCode extends Named
{
	public String code();
	
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
