
package one.microstream.demo.bookstore;

public interface Named extends Entity
{
	public String name();
	
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
