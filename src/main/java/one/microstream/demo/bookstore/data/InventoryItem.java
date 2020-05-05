package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requirePositive;

import java.util.Objects;

/**
 * View of a shop's inventory item slot.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface InventoryItem
{
	/**
	 * Get the shop which this inventory item belongs to
	 *
	 * @return the shop
	 */
	public Shop shop();

	/**
	 * Get the book of this inventory item
	 *
	 * @return the book
	 */
	public Book book();

	/**
	 * Get the amount of this inventory item
	 *
	 * @return the amount
	 */
	public int amount();


	/**
	 * Pseudo-constructor method to create a new {@link InventoryItem} instance with default implementation.
	 *
	 * @param shop not <code>null</code>
	 * @param book not <code>null</code>
	 * @param amount positive amount
	 * @return a new {@link InventoryItem} instance
	 */
	public static InventoryItem New(
		final Shop shop,
		final Book book,
		final int amount
	)
	{
		return new Default(
			Objects.requireNonNull(shop, () -> "Shop cannot be null"),
			Objects.requireNonNull(book, () -> "Book cannot be null"),
			requirePositive(amount, () -> "Amount must be greater than zero")
		);
	}


	/**
	 * Default implementation of the {@link InventoryItem} interface.
	 *
	 */
	public static class Default implements InventoryItem
	{
		private final Shop shop;
		private final Book book;
		private final int  amount;

		Default(
			final Shop shop,
			final Book book,
			final int amount
		)
		{
			super();
			this.shop   = shop;
			this.book   = book;
			this.amount = amount;
		}

		@Override
		public Shop shop()
		{
			return this.shop;
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

	}

}
