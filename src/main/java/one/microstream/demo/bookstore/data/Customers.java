package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.util.concurrent.ReadWriteLocked;
import one.microstream.storage.types.StorageConnection;

public interface Customers
{
	public int customerCount();

	public List<Customer> all();

	public <T> T compute(Function<Stream<Customer>, T> streamFunction);

	public Customer ofId(int customerId);

	public default void add(final Customer customer)
	{
		this.add(customer, BookStoreDemo.getInstance().storageManager());
	}

	public void add(Customer customer, StorageConnection storage);

	public default void addAll(final Collection<? extends Customer> customers)
	{
		this.addAll(customers, BookStoreDemo.getInstance().storageManager());
	}

	public void addAll(Collection<? extends Customer> customers, StorageConnection storage);


	public static class Default extends ReadWriteLocked.Scope implements Customers
	{
		private final Map<Integer, Customer> customers = new HashMap<>();

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
		public List<Customer> all()
		{
			return this.read(() ->
				new ArrayList<>(this.customers.values())
			);
		}

		@Override
		public <T> T compute(
			final Function<Stream<Customer>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(
					this.customers.values().parallelStream()
				)
			);
		}

		@Override
		public Customer ofId(final int customerId)
		{
			return this.read(() ->
				this.customers.get(customerId)
			);
		}

		@Override
		public void add(
			final Customer customer,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.customers.put(customer.customerId(), customer);
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
				this.customers.putAll(
					customers.stream().collect(
						Collectors.toMap(Customer::customerId, Function.identity())
					)
				);
				storage.store(this.customers);
			});
		}

	}

}
