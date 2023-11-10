package one.microstream.demo.bookstore;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the demo application variant with a Vaadin UI.
 * <p>
 * This is a Spring Boot application.
 * <p>
 * If you want to configure the amount of generated data, have a look at
 * {@link VaadinApplicationConfiguration#getBookStoreDemo()}.
 */
@SpringBootApplication
@Push
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@Slf4j
public class VaadinApplication implements AppShellConfigurator
{
	public static void main(final String[] args)
	{
		log.info("starting");
		SpringApplication.run(VaadinApplication.class, args);
	}
}
