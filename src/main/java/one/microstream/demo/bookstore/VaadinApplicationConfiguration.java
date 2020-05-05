package one.microstream.demo.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.microstream.demo.bookstore.data.RandomDataAmount;

@Configuration
public class VaadinApplicationConfiguration
{
	/**
	 * Manages the creation and disposal of the {@link BookStoreDemo} singleton.
	 */
	@Bean(destroyMethod = "shutdown")
	public BookStoreDemo getBookStoreDemo()
	{
		final BookStoreDemo demo = new BookStoreDemo(RandomDataAmount.Medium());
		demo.storageManager(); // eager init
		return demo;
	}
}
