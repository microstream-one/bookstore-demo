package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.util.Mutex;
import one.microstream.storage.types.StorageConnection;

public interface Customers
{
	public int customerCount();

	public <T> T compute(Function<Stream<Customer>, T> streamFunction);

	public void add(Customer customer, StorageConnection storage);

	public void addAll(Collection<? extends Customer> customers, StorageConnection storage);


	public static class Default extends Mutex.Owner implements Customers
	{
		private final List<Customer> customers = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public synchronized int customerCount()
		{
			return this.read(
				this.customers::size
			);
		}

		@Override
		public <T> T compute(
			final Function<Stream<Customer>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(
					this.customers.parallelStream()
				)
			);
		}

		@Override
		public void add(
			final Customer customer,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.customers.add(customer);
				storage.store(this.customers);
			});
		}

		@Override
		public void addAll(
			final Collection<? extends Customer> customers,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.customers.addAll(customers);
				storage.store(this.customers);
			});
		}

	}

}
