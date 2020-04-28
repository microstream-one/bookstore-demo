
package one.microstream.demo.bookstore.data;

public interface DataMetrics
{
	public int bookCount();

	public int countryCount();

	public int shopCount();


	public static DataMetrics New(
		final int bookCount,
		final int countryCount,
		final int shopCount
	)
	{
		return new Default(bookCount, countryCount, shopCount);
	}


	public static class Default implements DataMetrics
	{
		private final int    bookCount;
		private final int    countryCount;
		private final int    shopCount;

		Default(
			final int bookCount,
			final int countryCount,
			final int shopCount
		)
		{
			super();
			this.bookCount     = bookCount;
			this.countryCount  = countryCount;
			this.shopCount     = shopCount;
		}

		@Override
		public int bookCount()
		{
			return this.bookCount;
		}

		@Override
		public int countryCount()
		{
			return this.countryCount;
		}

		@Override
		public int shopCount()
		{
			return this.shopCount;
		}

		@Override
		public String toString()
		{
			return this.bookCount + " books, "
				+ this.shopCount + " shops in "
				+ this.countryCount + " countries";
		}

	}

}
