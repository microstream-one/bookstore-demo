package one.microstream.demo.bookstore;

import org.jsoup.nodes.Element;
import org.rapidpm.dependencies.core.logger.HasLogger;

import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;

/**
 * Service listener, registered via the Java service loader registry, which adds a logger as an error handler
 * and modifies the bootstrap page.
 */
@SuppressWarnings("serial")
public class VaadinApplicationInitListener
implements VaadinServiceInitListener, SessionInitListener, HasLogger
{
	public VaadinApplicationInitListener()
	{
		super();
	}

	@Override
	public void serviceInit(final ServiceInitEvent event)
	{
		event.getSource().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(final SessionInitEvent event) throws ServiceException
	{
		event.getSession().setErrorHandler(error ->
			this.logger().severe(error.getThrowable())
		);
	}

}
