
package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import one.microstream.storage.types.EmbeddedStorageManager;


public interface Data
{
	public Books books();
	
	public Stream<Shop> shops();
	
	public Stream<Customer> customers();
		
	public Purchases purchases();
	
	public DataMetrics populate(
		final RandomDataAmount initialDataSize,
		final EmbeddedStorageManager storageManager
	);
	
	public static Data New()
	{
		return new Default();
	}
	
	public static class Default implements Data
	{
		private final Books.Mutable     books     = new Books.Default();
		private final List<Shop>        shops     = new ArrayList<>(1024);
		private final List<Customer>    customers = new ArrayList<>(16384);
		private final Purchases.Mutable purchases = new Purchases.Default();
		
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
		public Stream<Shop> shops()
		{
			return this.shops.stream();
		}
		
		@Override
		public Stream<Customer> customers()
		{
			return this.customers.stream();
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
