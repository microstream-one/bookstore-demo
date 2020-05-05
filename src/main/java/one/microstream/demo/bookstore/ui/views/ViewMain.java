package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;

/**
 * Landing page of the web interface.
 *
 */
@Route(value = "", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewMain extends VerticalLayout
{
	public ViewMain(
		final BookStoreDemo bookStoreDemo
	)
	{
		super();

		final Image image = new Image("frontend/images/bookstoredemo.svg", "BookStore Demo");
		image.setWidth("80%");
		image.setMaxWidth("800px");
		image.setHeight(null);

		this.add(image);
		this.setSizeFull();
		this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		this.setJustifyContentMode(JustifyContentMode.CENTER);
	}

}
