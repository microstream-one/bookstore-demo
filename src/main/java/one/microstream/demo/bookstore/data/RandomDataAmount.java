
package one.microstream.demo.bookstore.data;

/**
 * Data amount boundaries for the {@link RandomDataGenerator}.
 *
 */
public interface RandomDataAmount
{
	public double minRatio();

	public int maxGenres();

	public int maxCountries();

	public int maxPublishersPerCountry();

	public int maxAuthorsPerCountry();

	public int maxBooksPerCountry();

	public int maxCitiesPerCountry();

	public int maxCustomersPerCity();

	public int maxShopsPerCity();

	public int maxBooksPerShop();

	public int maxAgeOfShopsInYears();

	public int maxEmployeesPerShop();

	public int maxPurchasesPerEmployeePerYear();

	public int maxItemsPerPurchase();


	public static RandomDataAmount valueOf(final String name)
	{
		switch(name.toLowerCase())
		{
			case "minimal"  : return Minimal();
			case "small"    : return Small();
			case "medium"   : return Medium();
			case "large"    : return Large();
			case "humongous": return Humongous();
		}

		throw new IllegalArgumentException("Invalid data amount: " + name
			+ ", supported values: minimal, small, medium, large, humongous");
	}


	public static RandomDataAmount Minimal()
	{
		return new Default(
			1.0, // minRatio
			1, // maxGenres
			1, // maxCountries
			1, // maxPublishersPerCountry
			1, // maxAuthorsPerCountry
			1, // maxBooksPerCountry
			1, // maxCitiesPerCountry
			1, // maxCustomersPerCity
			1, // maxShopsPerCity
			1, // maxBooksPerShop
			1, // maxAgeOfShopsInYears
			1, // maxEmployeesPerShop
			1, // maxPurchasesPerEmployeePerYear
			1 // maxItemsPerPurchase
		);
	}

	public static RandomDataAmount Small()
	{
		return new Default(
			0.0, // minRatio
			10, // maxGenres
			1, // maxCountries
			10, // maxPublishersPerCountry
			50, // maxAuthorsPerCountry
			100, // maxBooksPerCountry
			10, // maxCitiesPerCountry
			100, // maxCustomersPerCity
			3, // maxShopsPerCity
			250, // maxBooksPerShop
			10, // maxAgeOfShopsInYears
			5, // maxEmployeesPerShop
			100, // maxPurchasesPerEmployeePerYear
			3 // maxItemsPerPurchase
		);
	}

	public static RandomDataAmount Medium()
	{
		return new Default(
			0.3, // minRatio
			100, // maxGenres
			3, // maxCountries
			25, // maxPublishersPerCountry
			250, // maxAuthorsPerCountry
			500, // maxBooksPerCountry
			10, // maxCitiesPerCountry
			500, // maxCustomersPerCity
			4, // maxShopsPerCity
			350, // maxBooksPerShop
			15, // maxAgeOfShopsInYears
			7, // maxEmployeesPerShop
			150, // maxPurchasesPerEmployeePerYear
			3 // maxItemsPerPurchase
		);
	}

	public static RandomDataAmount Large()
	{
		return new Default(
			0.4, // minRatio
			500, // maxGenres
			5, // maxCountries
			50, // maxPublishersPerCountry
			500, // maxAuthorsPerCountry
			1000, // maxBooksPerCountry
			25, // maxCitiesPerCountry
			750, // maxCustomersPerCity
			5, // maxShopsPerCity
			500, // maxBooksPerShop
			20, // maxAgeOfShopsInYears
			10, // maxEmployeesPerShop
			150, // maxPurchasesPerEmployeePerYear
			3 // maxItemsPerPurchase
		);
	}

	public static RandomDataAmount Humongous()
	{
		return new Default(
			0.5, // minRatio
			500, // maxGenres
			5, // maxCountries
			50, // maxPublishersPerCountry
			500, // maxAuthorsPerCountry
			1000, // maxBooksPerCountry
			50, // maxCitiesPerCountry
			750, // maxCustomersPerCity
			5, // maxShopsPerCity
			500, // maxBooksPerShop
			100, // maxAgeOfShopsInYears
			10, // maxEmployeesPerShop
			150, // maxPurchasesPerEmployeePerYear
			3 // maxItemsPerPurchase
		);
	}

	public static class Default implements RandomDataAmount
	{
		private final double minRatio;
		private final int    maxGenres;
		private final int    maxCountries;
		private final int    maxPublishersPerCountry;
		private final int    maxAuthorsPerCountry;
		private final int    maxBooksPerCountry;
		private final int    maxCitiesPerCountry;
		private final int    maxCustomersPerCity;
		private final int    maxShopsPerCity;
		private final int    maxBooksPerShop;
		private final int    maxAgeOfShopsInYears;
		private final int    maxEmployeesPerShop;
		private final int    maxPurchasesPerEmployeePerYear;
		private final int    maxItemsPerPurchase;

		Default(
			final double minRatio,
			final int maxGenres,
			final int maxCountries,
			final int maxPublishersPerCountry,
			final int maxAuthorsPerCountry,
			final int maxBooksPerCountry,
			final int maxCitiesPerCountry,
			final int maxCustomersPerCity,
			final int maxShopsPerCity,
			final int maxBooksPerShop,
			final int maxAgeOfShopsInYears,
			final int maxEmployeesPerShop,
			final int maxPurchasesPerEmployeePerYear,
			final int maxItemsPerPurchase
		)
		{
			super();
			this.minRatio                       = minRatio;
			this.maxGenres                      = maxGenres;
			this.maxCountries                   = maxCountries;
			this.maxPublishersPerCountry        = maxPublishersPerCountry;
			this.maxAuthorsPerCountry           = maxAuthorsPerCountry;
			this.maxBooksPerCountry             = maxBooksPerCountry;
			this.maxCitiesPerCountry            = maxCitiesPerCountry;
			this.maxCustomersPerCity            = maxCustomersPerCity;
			this.maxShopsPerCity                = maxShopsPerCity;
			this.maxBooksPerShop                = maxBooksPerShop;
			this.maxAgeOfShopsInYears           = maxAgeOfShopsInYears;
			this.maxEmployeesPerShop            = maxEmployeesPerShop;
			this.maxPurchasesPerEmployeePerYear = maxPurchasesPerEmployeePerYear;
			this.maxItemsPerPurchase            = maxItemsPerPurchase;
		}

		@Override
		public double minRatio()
		{
			return this.minRatio;
		}

		@Override
		public int maxGenres()
		{
			return this.maxGenres;
		}

		@Override
		public int maxCountries()
		{
			return this.maxCountries;
		}

		@Override
		public int maxPublishersPerCountry()
		{
			return this.maxPublishersPerCountry;
		}

		@Override
		public int maxAuthorsPerCountry()
		{
			return this.maxAuthorsPerCountry;
		}

		@Override
		public int maxBooksPerCountry()
		{
			return this.maxBooksPerCountry;
		}

		@Override
		public int maxCitiesPerCountry()
		{
			return this.maxCitiesPerCountry;
		}

		@Override
		public int maxCustomersPerCity()
		{
			return this.maxCustomersPerCity;
		}

		@Override
		public int maxShopsPerCity()
		{
			return this.maxShopsPerCity;
		}

		@Override
		public int maxBooksPerShop()
		{
			return this.maxBooksPerShop;
		}

		@Override
		public int maxAgeOfShopsInYears()
		{
			return this.maxAgeOfShopsInYears;
		}

		@Override
		public int maxEmployeesPerShop()
		{
			return this.maxEmployeesPerShop;
		}

		@Override
		public int maxPurchasesPerEmployeePerYear()
		{
			return this.maxPurchasesPerEmployeePerYear;
		}

		@Override
		public int maxItemsPerPurchase()
		{
			return this.maxItemsPerPurchase;
		}

	}

}
