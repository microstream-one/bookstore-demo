
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;
import static one.microstream.demo.bookstore.data.Named.Validation.validateName;

/**
 * City entity which holds a name and a {@link State}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface City extends Named
{
	/**
	 * Get the state.
	 *
	 * @return the state
	 */
	public State state();


	/**
	 * Pseudo-constructor method to create a new {@link City} instance with default implementation.
	 *
	 * @param name not empty, {@link Named.Validation#validateName(String)}
	 * @param state not <code>null</code>
	 * @return a new {@link City} instance
	 */
	public static City New(
		final String name,
		final State state
	)
	{
		return new Default(
			validateName(name),
			requireNonNull(state, () -> "State cannot be null")
		);
	}


	/**
	 * Default implementation of the {@link City} interface.
	 *
	 */
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
