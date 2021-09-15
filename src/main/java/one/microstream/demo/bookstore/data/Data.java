
package one.microstream.demo.bookstore.data;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

/**
 * Root object for all data used by this application.
 * <p>
 * This is the entry point for the persisted object graph.
 * <p>
 * The data is divided into four sections:
 * <ul>
 * <li>{@link Books}</li>
 * <li>{@link Shops}</li>
 * <li>{@link Customers}</li>
 * <li>{@link Purchases}</li>
 * </ul>
 *
 * @see <a href="https://manual.docs.microstream.one/data-store/root-instances">MicroStream Reference Manual</a>
 */
public interface Data
{
	/**
	 * Get the {@link Books} instance of this data node.
	 * @return the {@link Books}
	 */
	public Books books();

	/**
	 * Get the {@link Shops} instance of this data node.
	 * @return the {@link Shops}
	 */
	public Shops shops();

	/**
	 * Get the {@link Customers} instance of this data node.
	 * @return the {@link Customers}
	 */
	public Customers customers();

	/**
	 * Get the {@link Purchases} instance of this data node.
	 * @return the {@link Purchases}
	 */
	public Purchases purchases();


	/**
	 * Pseudo-constructor method to create a new {@link Data} instance with default implementation.
	 *
	 * @return a new {@link Data} instance.
	 */
	public static Data.Default New()
	{
		return new Default();
	}


	/**
	 * Default implementation of the {@link Data} interface.
	 */
	public static class Default implements Data
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


		/**
		 * This method is used exclusively by the {@link BookStoreDemo}
		 * and it's not published by the {@link Data} interface.
		 */
		public DataMetrics populate(
			final RandomDataAmount initialDataSize,
			final EmbeddedStorageManager storageManager
		)
		{
			return RandomDataGenerator.New(
				this.books,
				this.shops,
				this.customers,
				this.purchases,
				initialDataSize,
				storageManager
			)
			.generate();
		}

	}

}
