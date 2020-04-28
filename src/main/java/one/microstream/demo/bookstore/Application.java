package one.microstream.demo.bookstore;

import one.microstream.demo.bookstore.data.RandomDataAmount;

public class Application
{
	public static void main(final String[] args)
	{
		final BookStoreDemo bookStoreDemo = new BookStoreDemo(
			RandomDataAmount.Medium()
		);

		new Repl(bookStoreDemo).run();
	}
}
