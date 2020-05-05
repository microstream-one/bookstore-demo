
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.ValidationUtils.requireZeroOrPositive;

import java.util.Objects;

/**
 * View of a book's sale numbers, with {@link Comparable} capabilities.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface BookSales extends Comparable<BookSales>
{
	/**
	 * Get the book
	 *
	 * @return the book
	 */
	public Book book();

	/**
	 * Get the amount
	 *
	 * @return the amount
	 */
	public int amount();

	@Override
	public default int compareTo(
		final BookSales other
	)
	{
		return Integer.compare(other.amount(), this.amount());
	}


	/**
	 * Pseudo-constructor method to create a new {@link BookSales} instance with default implementation.
	 *
	 * @param book not <code>null</code>
	 * @param amount zero or positive
	 * @return a new {@link BookSales} instance
	 */
	public static BookSales New(
		final Book book,
		final int amount
	)
	{
		return new Default(
			Objects.requireNonNull(book, () -> "Book cannot be null"),
			requireZeroOrPositive(amount, () -> "Amount cannot be negative")
		);
	}

	/**
	 * Default implementation of the {@link BookSales} interface.
	 *
	 */
	public static class Default implements BookSales
	{
		private final Book book;
		private final int  amount;

		Default(
			final Book book,
			final int amount
		)
		{
			super();
			this.book   = book;
			this.amount = amount;
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
		public String toString()
		{
			return "BookSales [book=" + this.book + ", amount=" + this.amount + "]";
		}

	}

}
