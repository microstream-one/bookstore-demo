
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;

/**
 * Address entity which holds two address lines, zip code and a {@link City}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Address
{
	/**
	 * Get the first address line.
	 *
	 * @return first address line
	 */
	public String address();

	/**
	 * Get the second address line.
	 *
	 * @return second address line
	 */
	public String address2();

	/**
	 * Get the zip code.
	 *
	 * @return zip code
	 */
	public String zipCode();

	/**
	 * Get the city.
	 *
	 * @return city
	 */
	public City city();


	/**
	 * Pseudo-constructor method to create a new {@link Address} instance with default implementation.
	 *
	 * @param address not <code>null</code>
	 * @param address2 not <code>null</code>
	 * @param zipCode not <code>null</code>
	 * @param city not <code>null</code>
	 * @return a new {@link Address} instance
	 */
	public static Address New(
		final String address,
		final String address2,
		final String zipCode,
		final City city
	)
	{
		return new Default(
			requireNonNull(address , () -> "Address cannot be null" ),
			requireNonNull(address2, () -> "Address2 cannot be null"),
			requireNonNull(zipCode , () -> "ZipCode cannot be null" ),
			requireNonNull(city    , () -> "City cannot be null"    )
		);
	}


	/**
	 * Default implementation of the {@link Address} interface.
	 *
	 */
	public static class Default implements Address
	{
		private final String address;
		private final String address2;
		private final String zipCode;
		private final City   city;

		Default(
			final String address,
			final String address2,
			final String zipCode,
			final City city
		)
		{
			super();
			this.address  = address;
			this.address2 = address2;
			this.zipCode  = zipCode;
			this.city     = city;
		}

		@Override
		public String address()
		{
			return this.address;
		}

		@Override
		public String address2()
		{
			return this.address2;
		}

		@Override
		public String zipCode()
		{
			return this.zipCode;
		}

		@Override
		public City city()
		{
			return this.city;
		}

		@Override
		public String toString()
		{
			return "Address [address=" + this.address + ", address2=" + this.address2 + ", zipCode=" + this.zipCode
				+ ", city=" + this.city + "]";
		}

	}

}
