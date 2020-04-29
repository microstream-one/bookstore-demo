package one.microstream.demo.bookstore.data;

public interface InventoryItem
{
	public Shop shop();

	public Book book();

	public int amount();


	public static InventoryItem New(
		final Shop shop,
		final Book book,
		final int amount
	)
	{
		return new Default(shop, book, amount);
	}


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
