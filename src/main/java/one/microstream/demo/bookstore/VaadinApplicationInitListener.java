package one.microstream.demo.bookstore;

import org.jsoup.nodes.Element;
import org.rapidpm.dependencies.core.logger.HasLogger;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;
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
implements VaadinServiceInitListener, SessionInitListener, BootstrapListener, HasLogger
{
	public VaadinApplicationInitListener()
	{
		super();
	}

	@Override
	public void serviceInit(final ServiceInitEvent event)
	{
		event.getSource().addSessionInitListener(this);
		event.addBootstrapListener(this);
	}

	@Override
	public void sessionInit(final SessionInitEvent event) throws ServiceException
	{
		event.getSession().setErrorHandler(error ->
			this.logger().severe(error.getThrowable())
		);
	}

	@Override
	public void modifyBootstrapPage(final BootstrapPageResponse response)
	{
		final Element head = response.getDocument().head();
		head.prependElement("link")
			.attr("href", "frontend/images/favicon.svg")
			.attr("rel", "icon")
			.attr("type", "image/svg+xml");
		head.prependElement("link")
			.attr("href", "frontend/images/favicon.icon")
			.attr("rel", "alternate icon");
	}
	
}
