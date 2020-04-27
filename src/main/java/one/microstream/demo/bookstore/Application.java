package one.microstream.demo.bookstore;

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
