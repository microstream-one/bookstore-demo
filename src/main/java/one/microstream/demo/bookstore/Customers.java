package one.microstream.demo.bookstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.storage.types.StorageConnection;

public interface Customers
{
	public int customerCount();

	public <T> T compute(Function<Stream<Customer>, T> streamFunction);


	public static interface Mutable extends Customers
	{
		public void add(Customer customer, StorageConnection storage);

		public void addAll(Collection<? extends Customer> customers, StorageConnection storage);
	}

	public static class Default implements Customers.Mutable
	{
		private final List<Customer> customers = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public synchronized int customerCount()
		{
			return this.customers.size();
		}

		@Override
		public synchronized <T> T compute(
			final Function<Stream<Customer>, T> streamFunction
		)
		{
			return streamFunction.apply(
				this.customers.parallelStream()
			);
		}

		@Override
		public synchronized void add(
			final Customer customer,
			final StorageConnection storage
		)
		{
			this.customers.add(customer);
			storage.store(this.customers);
		}

		@Override
		public synchronized void addAll(
			final Collection<? extends Customer> customers,
			final StorageConnection storage
		)
		{
			this.customers.addAll(customers);
			storage.store(this.customers);
		}

	}

}
