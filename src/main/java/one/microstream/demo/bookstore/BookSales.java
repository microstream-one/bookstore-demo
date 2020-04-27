
package one.microstream.demo.bookstore;

public class BookSales implements Comparable<BookSales>
{
	private final Book book;
	private final int  amount;

	public BookSales(
		final Book book,
		final int amount
	)
	{
		super();
		this.book   = book;
		this.amount = amount;
	}

	public Book book()
	{
		return this.book;
	}

	public int amount()
	{
		return this.amount;
	}

	@Override
	public int compareTo(
		final BookSales other
	)
	{
		return Integer.compare(other.amount, this.amount);
	}

	@Override
	public String toString()
	{
		return "BookSales [book=" + this.book + ", amount=" + this.amount + "]";
	}

}
