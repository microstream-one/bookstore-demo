
package one.microstream.demo.bookstore.data;

public interface BookSales extends Comparable<BookSales>
{
	public Book book();

	public int amount();


	public static BookSales New(
		final Book book,
		final int amount
	)
	{
		return new Default(book, amount);
	}

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
		public int compareTo(
			final BookSales other
		)
		{
			return Integer.compare(other.amount(), this.amount);
		}

		@Override
		public String toString()
		{
			return "BookSales [book=" + this.book + ", amount=" + this.amount + "]";
		}

	}

}
