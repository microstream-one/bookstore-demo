package one.microstream.demo.bookstore.ui.views;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.html.Label;
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
@SuppressWarnings("serial")
public class ViewError extends VerticalLayout implements HasErrorParameter<Exception>
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

		this.add(new Label(message));

		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}

}
