
package one.microstream.demo.bookstore.data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.money.MonetaryAmount;


public interface Purchase
{
	public static interface Item
	{
		public Book book();

		public int amount();

		public MonetaryAmount price();

		public MonetaryAmount itemTotal();


		public static Item New(
			final Book book,
			final int amount
		)
		{
			return new Default(book, amount);
		}


		public static class Default implements Item
		{
			private final Book           book;
			private final int            amount;
			private final MonetaryAmount price;

			Default(
				final Book book,
				final int amount
			)
			{
				super();
				this.book   = book;
				this.amount = amount;
				this.price  = book.retailPrice();
			}

			@Override
			public Book book()
			{
				return this.book;
			}

			@Override
			public int amount()
			{
				return this.amount;
			}

			@Override
			public MonetaryAmount price()
			{
				return this.price;
			}

			@Override
			public MonetaryAmount itemTotal()
			{
				return this.price.multiply(this.amount);
			}

		}

	}

	public Shop shop();

	public Employee employee();

	public Customer customer();

	public LocalDateTime timestamp();

	public Stream<Item> items();

	public int itemCount();

	public MonetaryAmount total();


	public static Purchase New(
		final Shop shop,
		final Employee employee,
		final Customer customer,
		final LocalDateTime timestamp,
		final List<Item> items
	)
	{
		return new Default(shop, employee, customer, timestamp, items.toArray(new Item[items.size()]));
	}


	public static class Default implements Purchase
	{
		private final Shop               shop;
		private final Employee           employee;
		private final Customer           customer;
		private final LocalDateTime      timestamp;
		private final Item[]             items;
		private transient MonetaryAmount total;

		Default(
			final Shop shop,
			final Employee employee,
			final Customer customer,
			final LocalDateTime timestamp,
			final Item[] items
		)
		{
			super();
			this.shop      = shop;
			this.employee  = employee;
			this.customer  = customer;
			this.timestamp = timestamp;
			this.items     = items;
		}

		@Override
		public Shop shop()
		{
			return this.shop;
		}

		@Override
		public Employee employee()
		{
			return this.employee;
		}

		@Override
		public Customer customer()
		{
			return this.customer;
		}

		@Override
		public LocalDateTime timestamp()
		{
			return this.timestamp;
		}

		@Override
		public Stream<Item> items()
		{
			return Arrays.stream(this.items);
		}

		@Override
		public int itemCount()
		{
			return this.items.length;
		}

		@Override
		public MonetaryAmount total()
		{
			if(this.total  == null)
			{
				MonetaryAmount total = null;
				for(final Item item : this.items)
				{
					total = total == null
						? item.itemTotal()
						: total.add(item.itemTotal());
				}
				this.total = total;
			}
			return this.total;
		}

	}

}
