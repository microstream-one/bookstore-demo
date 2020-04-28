
package one.microstream.demo.bookstore;

import one.microstream.storage.types.EmbeddedStorageManager;


public interface Data
{
	public Books books();

	public Shops shops();

	public Customers customers();

	public Purchases purchases();


	public static interface Mutable extends Data
	{
		public DataMetrics populate(
			final RandomDataAmount initialDataSize,
			final EmbeddedStorageManager storageManager
		);
	}


	public static class Default implements Data.Mutable
	{
		private final Books.Default     books     = new Books.Default();
		private final Shops.Default     shops     = new Shops.Default();
		private final Customers.Default customers = new Customers.Default();
		private final Purchases.Default purchases = new Purchases.Default();

		Default()
		{
			super();
		}

		@Override
		public Books books()
		{
			return this.books;
		}

		@Override
		public Shops shops()
		{
			return this.shops;
		}

		@Override
		public Customers customers()
		{
			return this.customers;
		}

		@Override
		public Purchases purchases()
		{
			return this.purchases;
		}

		@Override
		public DataMetrics populate(
			final RandomDataAmount initialDataSize,
			final EmbeddedStorageManager storageManager
		)
		{
			final DataMetrics metrics = new RandomDataGenerator(
				this.books,
				this.shops,
				this.customers,
				this.purchases,
				initialDataSize,
				storageManager
			)
			.generate();

			System.gc();

			return metrics;
		}

	}

}
