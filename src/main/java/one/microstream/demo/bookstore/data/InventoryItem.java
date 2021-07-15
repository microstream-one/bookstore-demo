package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requirePositive;

import java.util.Objects;

/**
 * View of a shop's inventory item slot.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class InventoryItem
{
	/**
	 * Constructor to create a new {@link InventoryItem} instance.
	 *
	 * @param shop not <code>null</code>
	 * @param book not <code>null</code>
	 * @param amount positive amount
	 */
	private final Shop shop  ;
	private final Book book  ;
	private final int  amount;

	public InventoryItem(
		final Shop shop  ,
		final Book book  ,
		final int  amount
	)
	{
		super();
		this.shop   = Objects.requireNonNull(shop, () -> "Shop cannot be null");
		this.book   = Objects.requireNonNull(book, () -> "Book cannot be null");
		this.amount = requirePositive(amount, () -> "Amount must be greater than zero");
	}
	
	/**
	 * Get the shop which this inventory item belongs to
	 *
	 * @return the shop
	 */
	public Shop shop()
	{
		return this.shop;
	}

	/**
	 * Get the book of this inventory item
	 *
	 * @return the book
	 */
	public Book book()
	{
		return this.book;
	}

	/**
	 * Get the amount of this inventory item
	 *
	 * @return the amount
	 */
	public int amount()
	{
		return this.amount;
	}

}
