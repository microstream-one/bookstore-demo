
package one.microstream.demo.bookstore.data;

import static one.microstream.X.notNull;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonEmpty;
import static one.microstream.demo.bookstore.util.ValidationUtils.requirePositive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.money.MonetaryAmount;

/**
 * Purchase entity which holds a {@link Shop}, {@link Employee},
 * {@link Customer}, timestamp and {@link Item}s.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Purchase
{
	/**
	 * Purchase item entity, which holds a {@link Book}, an amount and a price.
	 *
	 */
	public static interface Item
	{
		/**
		 * Get the book
		 *
		 * @return the book
		 */
		public Book book();

		/**
		 * Get the amount of books
		 *
		 * @return the amount
		 */
		public int amount();

		/**
		 * Get the price the book was sold for
		 *
		 * @return the price at the time the book was sold
		 */
		public MonetaryAmount price();

		/**
		 * Computes the total amount of the purchase item (price * amound)
		 *
		 * @return the total amount of this item
		 */
		public MonetaryAmount itemTotal();


		/**
		 * Pseudo-constructor method to create a new {@link Item} instance with default implementation.
		 *
		 * @param book not <code>null</code>
		 * @param amount positive amount
		 * @return the new {@link Item} instance
		 */
		public static Item New(
			final Book book,
			final int amount
		)
		{
			return new Default(
				notNull(book),
				requirePositive(amount, () -> "Amount must be greater than zero")
			);
		}


		/**
		 * Default implementation of the {@link Item} interface.
		 *
		 */
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

	/**
	 * Get the shop the purchase was made in
	 *
	 * @return the shop
	 */
	public Shop shop();

	/**
	 * Get the employee who sold
	 *
	 * @return the employee
	 */
	public Employee employee();

	/**
	 * Get the customer who made the purchase
	 *
	 * @return the customer
	 */
	public Customer customer();

	/**
	 * The timestamp the purchase was made at
	 *
	 * @return the timestamp
	 */
	public LocalDateTime timestamp();

	/**
	 * Get all {@link Item}s of this purchase
	 *
	 * @return a {@link Stream} of {@link Item}s
	 */
	public Stream<Item> items();

	public int itemCount();

	/**
	 * Computes the total of this purchase (sum of {@link Item#itemTotal()})
	 *
	 * @return the total amount
	 */
	public MonetaryAmount total();


	/**
	 * Pseudo-constructor method to create a new {@link Purchase} instance with default implementation.
	 *
	 * @param shop not <code>null</code>
	 * @param employee not <code>null</code>
	 * @param customer not <code>null</code>
	 * @param timestamp not <code>null</code>
	 * @param items not empty
	 * @return a new {@link Purchase} instance
	 */
	public static Purchase New(
		final Shop shop,
		final Employee employee,
		final Customer customer,
		final LocalDateTime timestamp,
		final List<Item> items
	)
	{
		return new Default(
			notNull(shop),
			notNull(employee),
			notNull(customer),
			notNull(timestamp),
			requireNonEmpty(items, () -> "at least one item required in purchase")
		);
	}


	/**
	 * Default implementation of the {@link Purchase} interface.
	 *
	 */
	public static class Default implements Purchase
	{
		private final Shop               shop;
		private final Employee           employee;
		private final Customer           customer;
		private final LocalDateTime      timestamp;
		private final List<Item>         items;
		private transient MonetaryAmount total;

		Default(
			final Shop shop,
			final Employee employee,
			final Customer customer,
			final LocalDateTime timestamp,
			final List<Item> items
		)
		{
			super();
			this.shop      = shop;
			this.employee  = employee;
			this.customer  = customer;
			this.timestamp = timestamp;
			this.items     = new ArrayList<>(items);
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
			return this.items.stream();
		}

		@Override
		public int itemCount()
		{
			return this.items.size();
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
