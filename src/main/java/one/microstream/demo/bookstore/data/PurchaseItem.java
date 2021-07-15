package one.microstream.demo.bookstore.data;

import static one.microstream.X.notNull;
import static one.microstream.demo.bookstore.util.ValidationUtils.requirePositive;

import javax.money.MonetaryAmount;

/**
 * Purchase item entity, which holds a {@link Book}, an amount and a price.
 *
 */
public class PurchaseItem
{
	private final Book           book  ;
	private final int            amount;
	private final MonetaryAmount price ;

	/**
	 * Constructor to create a new {@link PurchaseItem} instance.
	 *
	 * @param book not <code>null</code>
	 * @param amount positive amount
	 */
	public PurchaseItem(
		final Book book  ,
		final int  amount
	)
	{
		super();
		this.book   = notNull(book);
		this.amount = requirePositive(amount, () -> "Amount must be greater than zero");
		this.price  = book.retailPrice();
	}
	/**
	 * Get the book
	 *
	 * @return the book
	 */
	public Book book()
	{
		return this.book;
	}

	/**
	 * Get the amount of books
	 *
	 * @return the amount
	 */
	public int amount()
	{
		return this.amount;
	}

	/**
	 * Get the price the book was sold for
	 *
	 * @return the price at the time the book was sold
	 */
	public MonetaryAmount price()
	{
		return this.price;
	}

	/**
	 * Computes the total amount of the purchase item (price * amound)
	 *
	 * @return the total amount of this item
	 */
	public MonetaryAmount itemTotal()
	{
		return this.price.multiply(this.amount);
	}

}