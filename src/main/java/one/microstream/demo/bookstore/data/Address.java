
package one.microstream.demo.bookstore.data;

public interface Address
{
	public String address();

	public String address2();

	public String zipCode();

	public City city();

	public static Address New(
		final String address,
		final String address2,
		final String zipCode,
		final City city
	)
	{
		return new Default(address, address2, zipCode, city);
	}

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
