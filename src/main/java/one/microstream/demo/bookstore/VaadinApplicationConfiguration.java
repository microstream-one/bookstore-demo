package one.microstream.demo.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.microstream.demo.bookstore.data.RandomDataAmount;

@Configuration
public class VaadinApplicationConfiguration
{
	@Bean(destroyMethod = "shutdown")
	public BookStoreDemo getBookStoreDemo()
	{
		return new BookStoreDemo(RandomDataAmount.Medium());
	}
}
