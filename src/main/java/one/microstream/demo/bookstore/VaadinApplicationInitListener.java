package one.microstream.demo.bookstore;

import org.rapidpm.dependencies.core.logger.HasLogger;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

/**
 * Service listener, registered via the Java service loader registry, which adds a logger as an error handler.
 */
@SuppressWarnings("serial")
public class VaadinApplicationInitListener implements VaadinServiceInitListener, HasLogger
{
	public VaadinApplicationInitListener()
	{
		super();
	}

	@Override
	public void serviceInit(final ServiceInitEvent event)
	{
		event.getSource().addSessionInitListener(sessionInitEvent ->
			sessionInitEvent.getSession().setErrorHandler(error ->
				this.logger().severe(error.getThrowable())
			)
		);
	}
}
