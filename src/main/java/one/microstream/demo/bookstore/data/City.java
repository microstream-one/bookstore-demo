
package one.microstream.demo.bookstore.data;

public interface City extends Named
{
	public State state();
	
	public static City New(
		final String name,
		final State state
	)
	{
		return new Default(name, state);
	}
	
	public static class Default extends Named.Abstract implements City
	{
		private final State state;
		
		Default(
			final String name,
			final State state
		)
		{
			super(name);
			this.state = state;
		}
		
		@Override
		public State state()
		{
			return this.state;
		}
		
	}
	
}
