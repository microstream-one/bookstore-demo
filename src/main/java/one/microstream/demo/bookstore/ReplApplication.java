package one.microstream.demo.bookstore;

import one.microstream.demo.bookstore.data.RandomDataAmount;

/**
 * Entry point for the demo application variant with a simple console.
 *
 */
public class ReplApplication
{
	public static void main(final String[] args)
	{
		final BookStoreDemo bookStoreDemo = new BookStoreDemo(
			RandomDataAmount.Medium()
		);

		new Repl(bookStoreDemo).run();
	}
}
