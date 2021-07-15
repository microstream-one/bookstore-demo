
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;

/**
 * Address entity which holds two address lines, zip code and a {@link City}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Address
{
	private final String address ;
	private final String address2;
	private final String zipCode ;
	private final City   city    ;

	public Address(
		final String address ,
		final String address2,
		final String zipCode ,
		final City   city
	)
	{
		this.address  = requireNonNull(address , () -> "Address cannot be null" );
		this.address2 = requireNonNull(address2, () -> "Address2 cannot be null");
		this.zipCode  = requireNonNull(zipCode , () -> "ZipCode cannot be null" );
		this.city     = requireNonNull(city    , () -> "City cannot be null"    );
	}

	/**
	 * Get the first address line.
	 *
	 * @return first address line
	 */
	public String address()
	{
		return this.address;
	}

	/**
	 * Get the second address line.
	 *
	 * @return second address line
	 */
	public String address2()
	{
		return this.address2;
	}

	/**
	 * Get the zip code.
	 *
	 * @return zip code
	 */
	public String zipCode()
	{
		return this.zipCode;
	}

	/**
	 * Get the city.
	 *
	 * @return city
	 */
	public City city()
	{
		return this.city;
	}

	@Override
	public String toString()
	{
		return "Address"
			+ " [address="  + this.address
			+ ", address2=" + this.address2
			+ ", zipCode="  + this.zipCode
			+ ", city="     + this.city
			+ "]";
	}

}
