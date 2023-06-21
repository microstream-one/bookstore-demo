package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.html.Paragraph;
import jakarta.servlet.http.HttpServletResponse;

import org.rapidpm.dependencies.core.logger.HasLogger;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;

import one.microstream.chars.XChars;

/**
 * Generic view for internal server errors.
 *
 */
@Route(value = "error", layout = RootLayout.class)
public class ViewError extends VerticalLayout implements HasErrorParameter<Exception>, HasLogger
{
	public ViewError()
	{
		super();

		this.setSizeFull();
	}

	@Override
	public int setErrorParameter(
		final BeforeEnterEvent event,
		final ErrorParameter<Exception> parameter
	)
	{
		String message = parameter.getCustomMessage();
		if(XChars.isEmpty(message))
		{
			message = parameter.getCaughtException().getMessage();
		}
		if(message == null)
		{
			message = "";
		}

		this.logger().severe(message, parameter.getCaughtException());
		
		this.add(new Paragraph(message));

		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}

}
