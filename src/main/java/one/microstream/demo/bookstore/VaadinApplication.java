package one.microstream.demo.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Entry point for the demo application variant with a Vaadin UI.
 * <p>
 * This is a Spring Boot application.
 * <p>
 * If you want to configure the amount of generated data, have a look at
 * {@link VaadinApplicationConfiguration#getBookStoreDemo()}.
 */
@SpringBootApplication
public class VaadinApplication extends SpringBootServletInitializer
{
	public static void main(
		final String[] args
	)
	{
		SpringApplication.run(VaadinApplication.class, args);
	}
}
